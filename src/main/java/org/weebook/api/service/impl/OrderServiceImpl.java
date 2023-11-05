package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.weebook.api.dto.OrderDTO;
import org.weebook.api.dto.OrderFeedBackDto;
import org.weebook.api.dto.TKProductDto;
import org.weebook.api.dto.VoucherDTO;
import org.weebook.api.dto.mapper.NotificationMapper;
import org.weebook.api.dto.mapper.OrderMapper;
import org.weebook.api.entity.*;
import org.weebook.api.exception.StringException;
import org.weebook.api.repository.OrderRepository;
import org.weebook.api.repository.ProductRepository;
import org.weebook.api.repository.UserRepository;
import org.weebook.api.repository.VoucherRepository;
import org.weebook.api.service.OrderService;
import org.weebook.api.web.request.OrderFeedBackRequest;
import org.weebook.api.web.request.OrderItemRequest;
import org.weebook.api.web.request.OrderRequest;
import org.weebook.api.web.request.UpdateStatusOrderRequest;

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
    final UserRepository userRepo;
    final VoucherRepository voucherRepo;
    final ProductRepository productRepository;

    @Override
    public OrderDTO order(OrderRequest orderRequest) {
        Order order = orderMapper.requestOrderToEntity(orderRequest);

        //orderitem
        List<OrderItem> orderItems = orderItems(orderRequest.getOrderItemRequests(), order);
        order.getOrderItems().addAll(orderItems);
        order.setStatus("order");

        //user
        User user = userRepo.findById(orderRequest.getUser_id()).orElse(null);
        order.setUser(user);
        user.getOrders().add(order);

        Notification notification = notificationMapper.notification("Order", "Đặt đơn hàng thành công :" + Instant.now().toString(), "order", user);
        user.getNotifications().add(notification);

        //check voucher dc phép sử dụng
        BigDecimal voucherDiscountAmount = BigDecimal.ZERO;
        if (orderRequest.getCode() != null) {
            VoucherDTO voucherDTO = voucherRepo
                    .checkVoucherUse(user, orderRequest.getCode());
            String error = checkVoucher(voucherDTO, orderRequest.getTotalAmount(), orderRequest.getCode());
            if (!error.equals("")) {
                throw new StringException(error);
            }

            voucherDiscountAmount = voucherDTO.getDiscountAmount();
        }

        BigDecimal total_discount = voucherDiscountAmount
                .add(orderRequest.getDiscount_balance());
        if (total_discount.compareTo(orderRequest.getTotalAmount()) > 0) {
            total_discount = orderRequest.getTotalAmount();
        }
        order.setTotalDiscount(total_discount);

        OrderStatus orderStatus = orderMapper.buildOrderStatus(order, "order");
        order.getOrderStatuses().add(orderStatus);

        try {
            User save = userRepo.saveAndFlush(user);
            order.setUser(save);
            return orderMapper.entityOrderToDto(order);
        } catch (Exception e) {
            throw new StringException("Sản phẩm số lượng k đủ");
        }
    }


    @Override
    public OrderDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest) {
        Optional<Order> orderOptional = orderRepository.findById(updateStatusOrderRequest.getIdOrder());
        if (orderOptional.isEmpty()) {
            return null;
        }

        Order order = orderOptional.get();

        String message = "Đơn hàng " + updateStatusOrderRequest.getIdOrder() + " đang ở trạng thái : " + updateStatusOrderRequest.getStatus();
        OrderStatus orderStatus = orderMapper.buildOrderStatus(order, updateStatusOrderRequest.getStatus());
        order.getOrderStatuses().add(orderStatus);
        User user = order.getUser();
        Notification notification = notificationMapper.notification("Order", message, "order", user);
        user.getNotifications().add(notification);

        if (updateStatusOrderRequest.getStatus().equals("success")) {
            Transaction transaction = transaction(order);
            order.getTransactions().add(transaction);
            user.getTransactions().add(transaction);
        }

        if (updateStatusOrderRequest.getStatus().equals("cancel")) {
            Set<OrderItem> orderItems = order.getOrderItems();
            cancel(orderItems);
        }

        order.setStatus(updateStatusOrderRequest.getStatus());
        orderRepository.save(order);
        userRepo.save(user);

        return orderMapper.entityOrderToDto(order);
    }

    @Override
    public OrderFeedBackDto orderfeedback(OrderFeedBackRequest orderFeedBackRequest) {
        Order order = orderRepository.findById(orderFeedBackRequest.getIdOrder()).orElse(null);
        if (order == null) {
            throw new StringException("T đố m hack được.");
        }

        if (!order.getStatus().equals("success")) {
            throw new StringException("Đơn hàng chưa hoàn thành không thể đánh giá");
        }

        if (order.getOrderFeedbacks().size() > 0) {
            throw new StringException("Bạn đã đánh gi rồi.");
        }

        OrderFeedback orderFeedback = orderMapper.requestOrderFeedBackToEntity(orderFeedBackRequest, order);
        order.getOrderFeedbacks().add(orderFeedback);
        orderRepository.save(order);
        return orderMapper.entityOrderFeedBackToDto(orderFeedback);
    }

    @Override
    public List<OrderDTO> userFindByStatus(Long idUser, String status, Integer page) {
        List<Order> orders = orderRepository.userFindByStatus(idUser, status, PageRequest.of(page - 1, 5)).getContent();
        return orderMapper.entityOrderToDtos(orders);
    }

    @Override
    public List<OrderDTO> adminFindByStatus(String status, Integer page) {
        List<Order> orders = orderRepository.adminFindByStatus(status, PageRequest.of(page - 1, 5)).getContent();
        return orderMapper.entityOrderToDtos(orders);
    }

    @Override
    public List<TKProductDto> tkByOrder() {
        return null;
    }

    Transaction transaction(Order order) {
        User user = order.getUser();
        Transaction transactionOld = new Transaction();
        BigDecimal pre_trade_amount = BigDecimal.ZERO;
        if (user.getTransactions().size() > 0) {
            transactionOld = user.getTransactions().get(0);
            pre_trade_amount = transactionOld.getPostTradeAmount();
        }

        BigDecimal amount = order.getTotalAmount().subtract(order.getTotalDiscount());
        BigDecimal total_amount = amount.multiply(BigDecimal.valueOf(0.1));
        BigDecimal post_trade_amount = pre_trade_amount.add(total_amount);
        String action = "asdf";
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
            orderItems.add(orderMapper.requestItemToEntity(orderItemRequest, order));
        }
        return orderItems;
    }

    String checkVoucher(VoucherDTO voucherDTO, BigDecimal total_amount, String codeVoucher) {
        if (voucherDTO == null) {
            return "Voucher này chỉ dành cho người đuợc chọn : : " + codeVoucher;
        }
        if (voucherDTO.getCondition().compareTo(total_amount) > 0) {
            return "Bạn không đủ điều kiện sử dụng voucher : " + codeVoucher;
        }

//        if (!voucherDTO.getValidFrom().isBefore(Instant.now())) {
//            return "Voucher chưa đến ngày sử dụng voucher : " + codeVoucher;
//        }

        if (!voucherDTO.getValidTo().isAfter(Instant.now())) {
            return "Voucher quá hạn sử dụng voucher : " + codeVoucher;
        }
        return "";
    }
}
