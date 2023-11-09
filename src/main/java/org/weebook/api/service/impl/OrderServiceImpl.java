package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
import org.weebook.api.entity.*;
import org.weebook.api.exception.StringException;
import org.weebook.api.repository.*;
import org.weebook.api.service.OrderService;
import org.weebook.api.web.request.*;

import java.math.BigDecimal;
import java.time.Instant;
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

        if (!StringUtils.hasText(orderRequest.getCode())) {
            String code = orderRequest.getCode();
            Optional<Voucher> first = voucherRepository.findAll()
                    .stream()
                    .filter(v -> v.getCode().equals(code)
                            && (Objects.isNull(v.getUser()) || v.getUser().equals(user)))
                    .findFirst();

            first.ifPresent(voucher -> {
                checkVoucher(voucher, orderRequest.getTotalAmount(), orderRequest.getCode());
                order.setDiscountVoucher(first.get().getDiscountAmount());
            });
        }

        OrderStatus orderStatus = orderMapper.buildOrderStatus("order");
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
        order.getOrderStatuses().add(orderStatus);
        orderStatus = orderStatusRepository.save(orderStatus);
        order.getOrderStatuses().add(orderStatus);

        if (updateStatusOrderRequest.getStatus().equals("success")) {
            BigDecimal amount = order.getTotalAmount().subtract(order.getDiscountVoucher()).subtract(order.getDiscountBalance());
            BigDecimal total_amount = amount.multiply(BigDecimal.valueOf(0.1));
            Transaction transaction = transaction(order, total_amount);
            transactionRepository.save(transaction);
            orderRepository.updateBalanceUser(user.getId(), total_amount);
        }

        if (updateStatusOrderRequest.getStatus().equals("cancel")) {
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
    public List<OrderDTO> userFindByStatus(FindOrderStatusRequest findOrderStatusRequest) {
        Authentication currentUser = this.securityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't order as no Authentication object found in context for current user.");
        }

        String userName = currentUser.getName();
        User user = (User) userDetailsService.loadUserByUsername(userName);
        List<Order> orders = orderRepository
                    .userFindByStatus(user.getId(), findOrderStatusRequest.getStatus(), PageRequest.of(findOrderStatusRequest.getPagingRequest().getPageNumber() - 1, findOrderStatusRequest.getPagingRequest().getPageSize()));
        return orderMapper.entityOrderToDtos(orders);
    }

    @Override
    public List<OrderDTO> adminFindByStatus(FindOrderStatusRequest findOrderStatusRequest) {
        List<Order> orders = orderRepository.adminFindByStatus(findOrderStatusRequest.getStatus(), PageRequest.of(findOrderStatusRequest.getPagingRequest().getPageNumber()- 1, findOrderStatusRequest.getPagingRequest().getPageSize()));
        return orderMapper.entityOrderToDtos(orders);
    }

    @Override
    public TkDto tkByOrder(TkOrderRequest tkOrderRequest) {
        List<TKProductDto> tkProductDto;
        BigDecimal total;
        if (tkOrderRequest.getYearMonth().equals("All")) {
            tkProductDto = orderItemRepository.thongke(
                    "%" + tkOrderRequest.getNameProduct() + "%",
                    PageRequest.of(tkOrderRequest.getPage() - 1, 8));
            total = orderItemRepository.thongketotal(
                    "%" + tkOrderRequest.getNameProduct() + "%");
        } else {
            String[] monthYear = tkOrderRequest.getYearMonth().split("-");
            tkProductDto = orderItemRepository.thongke(
                    Integer.valueOf(monthYear[1]),
                    Integer.valueOf(monthYear[0]),
                    "%" + tkOrderRequest.getNameProduct() + "%",
                    PageRequest.of(tkOrderRequest.getPage() - 1, 8));

            total = orderItemRepository.thongketotal(Integer.valueOf(monthYear[1]), Integer.parseInt(monthYear[0]),
                    "%" + tkOrderRequest.getNameProduct() + "%");
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
