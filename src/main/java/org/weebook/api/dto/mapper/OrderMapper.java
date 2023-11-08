package org.weebook.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.weebook.api.dto.*;
import org.weebook.api.entity.*;
import org.weebook.api.web.request.OrderFeedBackRequest;
import org.weebook.api.web.request.OrderItemRequest;
import org.weebook.api.web.request.OrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Mapper(componentModel = "spring", uses = {VoucherMapper.class, ProductMapper.class},
        imports = {UUID.class, Instant.class, List.class, ArrayList.class, LinkedHashSet.class})
public interface OrderMapper {
    @Mapping(target = "order", source = "order")
    @Mapping(target = "id", ignore = true)
    OrderItem requestItemToEntity(OrderItemRequest orderItemRequest, Order order);

    Order requestOrderToEntity(OrderRequest orderRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    OrderFeedback requestOrderFeedBackToEntity(OrderFeedBackRequest orderFeedBackRequest, Order order);


    OrderFeedBackDto entitiOrderFeedBackToDto(OrderFeedback orderFeedback);

    UserDto entityUserToDto(User user);

    @Mapping(target = "orderFeedbacks", source = "order.orderFeedbacks", qualifiedByName = "orderfeedback")
    OrderDTO entityOrderToDto(Order order);
    List<OrderDTO> entityOrderToDtos(List<Order> order);

    @Mapping(target = "order", source = "order")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", ignore = true)
    OrderStatus buildOrderStatus(Order order, String status);

    Set<OrderItemDTO> entityOrderItemToDtos(Set<OrderItem> orderItem);
    OrderItemDTO entityOrderItemToDto(OrderItem orderItem);

    OrderStatusDTO entityOrderStatusToDto(OrderStatus orderStatus);

    Set<OrderStatusDTO> entityOrderStatusToDtos(Set<OrderStatus> orderStatus);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "id", ignore = true)
    Transaction transaction(User user, Order order, String action, BigDecimal preTradeAmount, BigDecimal amount, BigDecimal postTradeAmount);



    @Named("orderfeedback")
    default OrderFeedBackDto orderfeedback(List<OrderFeedback> orderFeedbacks) {
        if(orderFeedbacks.size() == 0){
            return null;
        }
        OrderFeedback orderFeedback = orderFeedbacks.get(0);
        return entitiOrderFeedBackToDto(orderFeedback);
    }

}
