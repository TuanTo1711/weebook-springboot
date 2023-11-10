package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.weebook.api.dto.*;
import org.weebook.api.dto.mapper.NotificationMapper;
import org.weebook.api.dto.mapper.OrderMapper;
import org.weebook.api.dto.mapper.ProductMapper;
import org.weebook.api.entity.*;
import org.weebook.api.exception.StringException;
import org.weebook.api.projection.OrderStatusProjection;
import org.weebook.api.repository.*;
import org.weebook.api.service.OrderService;
import org.weebook.api.web.request.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final NotificationMapper notificationMapper;
    private final VoucherRepository voucherRepository;
    private final ProductRepository productRepository;
    private final NotificationRepository notificationRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderItemRepository orderItemRepository;
    private final TransactionRepository transactionRepository;
    private final OrderFeedBackRepository orderFeedBackRepository;
    private final ProductMapper productMapper;

    private final SecurityContextHolderStrategy securityContextHolder
            = SecurityContextHolder.getContextHolderStrategy();
    private final UserDetailsService userDetailsService;

    @Transactional
    @Override
    public OrderDetailDTO sendOrder(OrderRequest orderRequest) {
        Order order = orderMapper.requestOrderToEntity(orderRequest, "Ordering");

        Authentication currentUser = this.securityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        String userName = currentUser.getName();
        User user = (User) userDetailsService.loadUserByUsername(userName);
        order.setUser(user);
        if(user.getBalance().compareTo(orderRequest.getDiscountBalance()) < 0){
            throw new StringException("Bạn không đủ điểm để sử dụng");
        }
        order.setDiscountVoucher(BigDecimal.ZERO);

        String code = orderRequest.getCode();
        if (!code.equals("")) {
            Optional<Voucher> first = voucherRepository.checkVoucherUse(user, code);
            first.ifPresent(voucher -> {

                checkVoucher(voucher, orderRequest.getTotalAmount(), orderRequest.getCode());
                BigDecimal voucher_discount = first.get().getDiscountAmount();
                if(first.get().getType().equals("%")){
                    BigDecimal percent = voucher_discount.divide(BigDecimal.valueOf(100.0));
                    order.setDiscountVoucher(orderRequest.getTotalAmount().multiply(percent));
                }else {
                    order.setDiscountVoucher(voucher_discount);
                }
            });
        }

        OrderStatus orderStatus = orderMapper.buildOrderStatus("Ordering");
        order.getOrderStatuses().add(orderStatus);
        order.getOrderItems().forEach(item -> item.setOrder(order));
        orderStatus.setOrder(order);

        Order saved = orderRepository.saveAndFlush(order);

        if(orderRequest.getDiscountBalance().compareTo(BigDecimal.ZERO) > 0){
            Transaction transaction = transaction(saved, orderRequest.getDiscountBalance().negate());
            transactionRepository.save(transaction);
            orderRepository.updateBalanceUser(user.getId(), orderRequest.getDiscountBalance().negate());
        }

        Notification notification = notificationMapper.notification(
                "Thông báo về đơn hàng",
                "Đặt đơn hàng thành công vào lúc: " + Instant.now().toString()
                        + ". Mã đơn hàng: " + saved.getId(),
                "order",
                user);
        notificationRepository.save(notification);
        return orderMapper.entityOrderDetailToDto(saved);
    }


    @Transactional
    @Override
    public OrderDetailDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest) {
        String status = updateStatusOrderRequest.getStatus();
        Optional<Order> orderOptional = orderRepository.findById(updateStatusOrderRequest.getIdOrder());
        if (orderOptional.isEmpty()) {
            throw new StringException("Không có order này.");
        }

        Order order = orderOptional.get();

        order.setStatus(updateStatusOrderRequest.getStatus());
        orderRepository.save(order);

        User user = order.getUser();
        String message = "Đơn hàng " + updateStatusOrderRequest.getIdOrder() + " đang ở trạng thái : " + updateStatusOrderRequest.getStatus();
        Notification notification = notificationMapper.notification("Order", message, "order", user);
        notificationRepository.save(notification);

        OrderStatus orderStatus = orderMapper.buildOrderStatus(updateStatusOrderRequest.getStatus());
        orderStatus.setOrder(order);
        order.getOrderStatuses().add(orderStatus);
        orderStatus = orderStatusRepository.save(orderStatus);
        order.getOrderStatuses().add(orderStatus);

        if (status.equals("success")) {
            BigDecimal amount = order.getTotalAmount().subtract(order.getDiscountVoucher()).subtract(order.getDiscountBalance());
            BigDecimal total_amount = amount.multiply(BigDecimal.valueOf(0.1));
            Transaction transaction = transaction(order, total_amount);
            transactionRepository.save(transaction);
            orderRepository.updateBalanceUser(user.getId(), total_amount);
        }

        if (status.equals("cancel") || status.equals("bom")) {
            Set<OrderItem> orderItems = order.getOrderItems();
            cancel(orderItems);
            if(order.getDiscountBalance().compareTo(BigDecimal.ZERO) > 0){
                Transaction transaction = transaction(order, order.getDiscountBalance());
                transactionRepository.save(transaction);
                orderRepository.updateBalanceUser(user.getId(), order.getDiscountBalance());
            }
        }

        return orderMapper.entityOrderDetailToDto(order);
    }

    @Override
    public OrderFeedBackDto orderFeedback(OrderFeedBackRequest orderFeedBackRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderFeedBackRequest.getIdOrder());
        if (orderOptional.isEmpty()) {
            throw new StringException("T đố m hack được.");
        }

        Order order = orderOptional.get();

        if (!order.getStatus().equals("success")) {
            throw new StringException("Đơn hàng chưa hoàn thành không thể đánh giá");
        }

        if (!order.getOrderFeedbacks().isEmpty()) {
            throw new StringException("Bạn đã đánh gi rồi.");
        }

        OrderFeedback orderFeedback = orderMapper.requestOrderFeedBackToEntity(orderFeedBackRequest, order);
        orderFeedBackRepository.save(orderFeedback);
        return orderMapper.entityOrderFeedBackToDto(orderFeedback);
    }

    @Override
    public List<OrderStatusProjection> userFindByStatus(String status, PagingRequest pagingRequest) {
        Authentication currentUser = this.securityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        Pageable pageable = PageRequest.of(pagingRequest.getPageNumber() - 1,
                pagingRequest.getPageSize());

        String userName = currentUser.getName();
        User user = (User) userDetailsService.loadUserByUsername(userName);
        if(status.equals("All")){
            return orderStatusRepository.userGetAllStatus(user.getId(), OrderStatusProjection.class, pageable);
        }
        return orderStatusRepository.userGetAllStatus(status, user.getId(), OrderStatusProjection.class, pageable);
    }

    @Override
    public List<OrderDTO> adminFindByStatus(String status, PagingRequest pagingRequest) {
        List<Order> orders = orderRepository.adminFindByStatus(status, PageRequest.of(pagingRequest.getPageNumber()- 1, pagingRequest.getPageSize()));
        return orderMapper.entityOrderToDtos(orders);
    }

    @Override
    public TkDto tkByOrder(String yearMonth, String nameProduct, PagingRequest pagingRequest) {
        List<TKProductDto> tkProductDto;
        BigDecimal total;
        Pageable pageable = PageRequest.of(pagingRequest.getPageNumber() - 1, pagingRequest.getPageSize());
        if (yearMonth.equals("All")) {
            tkProductDto = orderItemRepository.thongke(
                    "%" + nameProduct + "%",
                   pageable );
            total = orderItemRepository.thongketotal(
                    "%" + nameProduct + "%");
        } else {
            String[] monthYear = yearMonth.split("-");
            tkProductDto = orderItemRepository.thongke(
                    Integer.valueOf(monthYear[1]),
                    Integer.valueOf(monthYear[0]),
                    "%" + nameProduct + "%",
                    pageable);

            total = orderItemRepository.thongketotal(Integer.valueOf(monthYear[1]), Integer.parseInt(monthYear[0]),
                    "%" + nameProduct + "%");
        }
        return TkDto
                .builder()
                .total(total)
                .productDto(tkProductDto)
                .build();
    }

    @Override
    public List<String> getYearMonth() {
        List<String> yearMonth = new ArrayList<>();
        yearMonth.add("All");
        yearMonth.addAll(orderRepository.findAllMonthYear());
        return yearMonth;
    }

    @Override
    public OrderDetailDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        return orderMapper.entityOrderDetailToDto(order);
    }

    @Override
    public List<ProductInfo> trend(LocalDate dateMin, LocalDate dateMax, PagingRequest pagingRequest) {
        Pageable pageable = PageRequest.of(pagingRequest.getPageNumber()-1, pagingRequest.getPageSize());
        List<Product> products = productRepository.trend(dateMin, dateMax, pageable);
        return productMapper.toInfos(products);
    }

    @Override
    public void addOrder() {
        for (int i= 10; i<1000000;i++){
            Order order = Order
                    .builder()
                    .discountBalance(BigDecimal.ZERO)
                    .discountVoucher(BigDecimal.ZERO)
                    .totalAmount(BigDecimal.ZERO)
                    .status(status())
                    .user(User.builder().id((long)i).build())
                    .build();
            orderRepository.save(order);
        }
    }

    public String status(){
        Random random = new Random();
        int count =  random.nextInt(4);
        switch (count){
            case 0 : return "Ordering";
            case 1 : return "confirm";
            case 2 : return "ship";
            case 3 : return "success";
            case 4 : return "cancel";
            default: return "bom";
        }
    }

    Transaction transaction(Order order, BigDecimal amount) {
        User user = order.getUser();
        Transaction transactionOld = new Transaction();
        BigDecimal pre_trade_amount = BigDecimal.ZERO;
        if (!user.getTransactions().isEmpty()) {
            transactionOld = user.getTransactions().get(0);
            pre_trade_amount = transactionOld.getPostTradeAmount();
        }
        BigDecimal post_trade_amount = pre_trade_amount.add(amount);
        String action = "Order";
        return orderMapper.transaction(user, order, action, pre_trade_amount, amount, post_trade_amount);
    }


    void cancel(Set<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(new Product());
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productRepository.save(product);
        }
    }

    List<OrderItem> orderItems(List<OrderItemRequest> orderItemRequests, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            try {
                OrderItem orderItem = orderMapper.requestItemToEntity(orderItemRequest, order);
                orderItem = orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
            } catch (Exception e) {
                throw new StringException("Sản phẩm số lượng k đủ");
            }
        }
        return orderItems;
    }

    void checkVoucher(Voucher voucher, BigDecimal totalAmount, String codeVoucher) {
        if (voucher.getCondition().compareTo(totalAmount) > 0) {
            throw new StringException("Bạn không đủ điều kiện sử dụng voucher: " + codeVoucher);
        }

        if (!voucher.getValidFrom().isBefore(Instant.now())) {
            throw new StringException("Voucher chưa đến ngày sử dụng voucher: " + codeVoucher);
        }

        if (!voucher.getValidTo().isAfter(Instant.now())) {
            throw new StringException("Voucher quá hạn sử dụng voucher : " + codeVoucher);
        }
    }
}
