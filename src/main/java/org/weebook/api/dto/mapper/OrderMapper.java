package org.weebook.api.dto.mapper;

import org.mapstruct.*;
import org.weebook.api.dto.OrderDTO;
import org.weebook.api.dto.OrderFeedBackDto;
import org.weebook.api.dto.OrderItemDTO;
import org.weebook.api.dto.OrderStatusDTO;
import org.weebook.api.entity.*;
import org.weebook.api.web.request.OrderFeedBackRequest;
import org.weebook.api.web.request.OrderItemRequest;
import org.weebook.api.web.request.OrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ProductMapper.class, UserMapper.class},
        imports = {UUID.class, Instant.class, List.class, ArrayList.class, LinkedHashSet.class})
public interface OrderMapper {
    @Mapping(target = "order", source = "order")
    @Mapping(target = "product", source = "orderItemRequest.product_id", qualifiedByName = "mapIdToProduct")
    @Mapping(target = "id", ignore = true)
    OrderItem requestItemToEntity(OrderItemRequest orderItemRequest, Order order);


    @Mapping(target = "orderItems", expression = "java(new LinkedHashSet<>())")
    @Mapping(target = "orderStatuses", expression = "java(new ArrayList<>())")
    @Mapping(target = "orderDate", expression = "java(Instant.now())")
    Order requestOrderToEntity(OrderRequest orderRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    OrderFeedback requestOrderFeedBackToEntity(OrderFeedBackRequest orderFeedBackRequest, Order order);


    OrderFeedBackDto entityOrderFeedBackToDto(OrderFeedback orderFeedback);

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


    @Named("mapIdToProduct")
    default Product mapIdToProduct(Long id) {
        if (id != null) {
            Product product = new Product();
            product.setId(id);
            return product;
        }
        return null;
    }

    @Named("orderfeedback")
    default OrderFeedBackDto orderfeedback(List<OrderFeedback> orderFeedbacks) {
        if (orderFeedbacks.size() == 0) {
            return null;
        }
        OrderFeedback orderFeedback = orderFeedbacks.get(0);
        return entityOrderFeedBackToDto(orderFeedback);
    }

}
