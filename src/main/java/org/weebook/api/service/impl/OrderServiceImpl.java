package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weebook.api.dto.OrderDTO;
import org.weebook.api.dto.OrderFeedBackDto;
import org.weebook.api.dto.TKProductDto;
import org.weebook.api.dto.TkDto;
import org.weebook.api.dto.mapper.NotificationMapper;
import org.weebook.api.dto.mapper.OrderMapper;
import org.weebook.api.entity.*;
import org.weebook.api.exception.StringException;
import org.weebook.api.repository.*;
import org.weebook.api.service.OrderService;
import org.weebook.api.web.request.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    final OrderRepository orderRepository;
    final OrderMapper orderMapper;
    final NotificationMapper notificationMapper;
    final UserRepository userRepository;
    final VoucherRepository voucherRepository;
    final ProductRepository productRepository;
    final NotificationRepository notificationRepository;
    final OrderStatusRepository orderStatusRepository;
    final OrderItemRepository orderItemRepository;
    final TransactionRepository transactionRepository;
    final OrderFeedBackRepository orderFeedBackRepository;

    @Transactional
    @Override
    public OrderDTO order(OrderRequest orderRequest){
        Order order = orderMapper.requestOrderToEntity(orderRequest);
        order.setStatus("order");

        //user
        Optional<User> optionalUser = userRepository.findById(orderRequest.getUser_id());
        if(optionalUser.isEmpty()){
            throw new StringException("Không có tài khoản này");
        }
        User user = optionalUser.get();
        order.setUser(user);

        //check voucher dc phép sử dụng
        BigDecimal voucherDiscountAmount = BigDecimal.ZERO;
        if(orderRequest.getCode() != null){
            List<Voucher> vouchers = voucherRepository
                    .checkVoucherUse(user,orderRequest.getCode());
            Voucher voucher = checkVoucher(vouchers, orderRequest.getTotalAmount(), orderRequest.getCode());
            voucherDiscountAmount = voucher.getDiscountAmount();
        }

        BigDecimal total_discount = voucherDiscountAmount
                .add(orderRequest.getDiscount_balance());
        if(total_discount.compareTo(orderRequest.getTotalAmount()) > 0){
            total_discount = orderRequest.getTotalAmount();
        }
        order.setTotalDiscount(total_discount);

        order = orderRepository.save(order);

        Notification notification = notificationMapper.notification("Order","Đặt đơn hàng thành công :" + Instant.now().toString()+" /Mã đơn hàng : "+order.getId(),"order",user);
        notificationRepository.save(notification);

        OrderStatus orderStatus = orderMapper.buildOrderStatus(order,"order");
        orderStatus = orderStatusRepository.save(orderStatus);
        order.getOrderStatuses().add(orderStatus);


        //orderitem
        List<OrderItem> orderItems = orderItems(orderRequest.getOrderItemRequests(),order);
        order.getOrderItems().addAll(orderItems);
        return orderMapper.entityOrderToDto(order);
    }


    @Transactional
    @Override
    public OrderDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest) {
        Optional<Order> orderOptional = orderRepository.findById(updateStatusOrderRequest.getIdOrder());
        if(orderOptional.isEmpty()){
            throw  new StringException("Không có order này.");
        }

        Order order = orderOptional.get();

        order.setStatus(updateStatusOrderRequest.getStatus());
        orderRepository.save(order);

        User user = order.getUser();
        String message = "Đơn hàng "+ updateStatusOrderRequest.getIdOrder() + " đang ở trạng thái : "+updateStatusOrderRequest.getStatus();
        Notification notification = notificationMapper.notification("Order",message, "order", user);
        notificationRepository.save(notification);

        OrderStatus orderStatus = orderMapper.buildOrderStatus(order, updateStatusOrderRequest.getStatus());
        order.getOrderStatuses().add(orderStatus);
        orderStatus = orderStatusRepository.save(orderStatus);
        order.getOrderStatuses().add(orderStatus);

        if(updateStatusOrderRequest.getStatus().equals("success")){
            Transaction transaction = transaction(order);
            transactionRepository.save(transaction);
        }

        if(updateStatusOrderRequest.getStatus().equals("cancel")){
            Set<OrderItem> orderItems = order.getOrderItems();
            cancel(orderItems);
        }

        return orderMapper.entityOrderToDto(order);
    }

    @Override
    public OrderFeedBackDto orderfeedback(OrderFeedBackRequest orderFeedBackRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderFeedBackRequest.getIdOrder());
        if(orderOptional.isEmpty()){
            throw new StringException("T đố m hack được.");
        }

        Order order = orderOptional.get();

        if (!order.getStatus().equals("success")){
            throw new StringException("Đơn hàng chưa hoàn thành không thể đánh giá");
        }

        if(order.getOrderFeedbacks().size()>0){
            throw new StringException("Bạn đã đánh gi rồi.");
        }

        OrderFeedback orderFeedback = orderMapper.requestOrderFeedBackToEntity(orderFeedBackRequest, order);
        orderFeedBackRepository.save(orderFeedback);
        return orderMapper.entitiOrderFeedBackToDto(orderFeedback);
    }

    @Override
    public List<OrderDTO> userFindByStatus(Long idUser, String status, Integer page) {
        List<Order> orders = orderRepository.userFindByStatus( idUser,status, PageRequest.of(page-1,5));
        return orderMapper.entityOrderToDtos(orders);
    }

    @Override
    public List<OrderDTO> adminFindByStatus(String status, Integer page) {
        List<Order> orders = orderRepository.adminFindByStatus(status, PageRequest.of(page-1,5));
        return orderMapper.entityOrderToDtos(orders);
    }

    @Override
    public TkDto tkByOrder(TkOrderRequest tkOrderRequest) {
        String[] monthYear = tkOrderRequest.getYearMonth().split("-");
        List<TKProductDto> tkProductDto = orderItemRepository.thongke(
                Integer.valueOf(monthYear[1]),Integer.valueOf(monthYear[0]),
                "%"+tkOrderRequest.getNameProduct()+"%",
                PageRequest.of(tkOrderRequest.getPage()-1,8));

        BigDecimal total = orderItemRepository.thongketotal(Integer.valueOf(monthYear[1]),Integer.valueOf(monthYear[0]),
                "%"+tkOrderRequest.getNameProduct()+"%");
        return TkDto
                .builder()
                .total(total)
                .productDto(tkProductDto)
                .build();
    }

    @Override
    public List<String> getYearMonth() {
        return orderRepository.findAllMonthYear();
    }

    Transaction transaction(Order order){
        User user = order.getUser();
        Transaction transactionOld = new Transaction();
        BigDecimal pre_trade_amount = BigDecimal.ZERO;
        if(user.getTransactions().size() > 0){
            transactionOld = user.getTransactions().get(0);
            pre_trade_amount = transactionOld.getPostTradeAmount();
        }

        BigDecimal amount = order.getTotalAmount().subtract(order.getTotalDiscount());
        BigDecimal total_amount = amount.multiply(BigDecimal.valueOf(0.1));
        BigDecimal post_trade_amount = pre_trade_amount.add(total_amount);
        String action = "Order";
        return orderMapper.transaction(user,order,action, pre_trade_amount, amount, post_trade_amount);
    }


    void cancel(Set<OrderItem> orderItems){
        for (OrderItem orderItem: orderItems){
            Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(new Product());
            product.setQuantity(product.getQuantity()+orderItem.getQuantity());
            productRepository.save(product);
        }
    }

    List<OrderItem> orderItems(List<OrderItemRequest> orderItemRequests, Order order){
        List<OrderItem> orderItems  = new ArrayList<>();
        for (OrderItemRequest orderItemRequest: orderItemRequests){
            try {
                OrderItem orderItem = orderMapper.requestItemToEntity(orderItemRequest,order);
                orderItem = orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
            }catch (Exception e){
                throw new StringException("Sản phẩm số lượng k đủ");
            }
        }
        return orderItems;
    }

    Voucher checkVoucher(List<Voucher> vouchers, BigDecimal total_amount, String codeVoucher){
        if(vouchers.size() == 0){
            throw new StringException("Voucher này chỉ dành cho người đuợc chọn : : " + codeVoucher);
        }
        Voucher voucher = vouchers.get(0);
        if(voucher.getCondition().compareTo(total_amount) > 0){
            throw new StringException("Bạn không đủ điều kiện sử dụng voucher : "+codeVoucher);
        }

        if(!voucher.getValidFrom().isBefore(Instant.now())){
            throw new StringException("Voucher chưa đến ngày sử dụng voucher : "+codeVoucher);
        }

        if(!voucher.getValidTo().isAfter(Instant.now())){
            throw new StringException("Voucher quá hạn sử dụng voucher : "+codeVoucher);
        }
        return voucher;
    }
}
