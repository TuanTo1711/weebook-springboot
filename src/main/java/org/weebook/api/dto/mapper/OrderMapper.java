package org.weebook.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.weebook.api.dto.*;
import org.weebook.api.entity.*;
import org.weebook.api.web.request.OrderFeedBackRequest;
import org.weebook.api.web.request.OrderItemRequest;
import org.weebook.api.web.request.OrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {VoucherMapper.class, ProductMapper.class, UserMapper.class},
        imports = {UUID.class, Instant.class, List.class, ArrayList.class, LinkedHashSet.class})
public interface OrderMapper {
    @Mapping(target = "order", source = "order")
    @Mapping(target = "id", ignore = true)
    OrderItem requestItemToEntity(OrderItemRequest orderItemRequest, Order order);

    @Mapping(target = "orderItems", source = "orderRequest.orderItemRequests")
    Order requestOrderToEntity(OrderRequest orderRequest, String status);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    OrderFeedback requestOrderFeedBackToEntity(OrderFeedBackRequest orderFeedBackRequest, Order order);

    OrderFeedBackDto entityOrderFeedBackToDto(OrderFeedback orderFeedback);

    @Mapping(target = "orderFeedbacks", source = "order.orderFeedbacks", qualifiedByName = "order-feedback")
    OrderDetailDTO entityOrderDetailToDto(Order order);

    List<OrderDetailDTO> entityOrderDetailToDtos(List<Order> order);

    OrderDTO entityOrderToDto(Order order);

    List<OrderDTO> entityOrderToDtos(List<Order> order);

    OrderStatus buildOrderStatus(String status);


    Set<OrderItemDTO> entityOrderItemToDtos(Set<OrderItem> orderItem);

    OrderItemDTO entityOrderItemToDto(OrderItem orderItem);

    OrderStatusDTO entityOrderStatusToDto(OrderStatus orderStatus);

    Set<OrderStatusDTO> entityOrderStatusToDtos(Set<OrderStatus> orderStatus);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "id", ignore = true)
    Transaction transaction(User user, Order order, String action, BigDecimal preTradeAmount, BigDecimal amount, BigDecimal postTradeAmount);


    TransactionDto entityTransactionToDto(Transaction transaction);

    List<TransactionDto> entityTransactionToDtos(List<Transaction> transaction);

    @Named("order-feedback")
    default OrderFeedBackDto orderFeedback(List<OrderFeedback> orderFeedbacks) {
        if (orderFeedbacks.isEmpty()) {
            return null;
        }
        OrderFeedback orderFeedback = orderFeedbacks.get(0);
        return entityOrderFeedBackToDto(orderFeedback);
    }

}
