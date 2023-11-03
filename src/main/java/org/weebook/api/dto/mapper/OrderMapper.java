package org.weebook.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.weebook.api.dto.*;
import org.weebook.api.entity.*;
import org.weebook.api.web.request.OrderItemRequest;
import org.weebook.api.web.request.OrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Mapper(componentModel = "spring",
        imports = {UUID.class, Instant.class, List.class, ArrayList.class, LinkedHashSet.class})
public interface OrderMapper {
    @Mapping(target = "order", source = "order")
    @Mapping(target = "product", source = "orderItemRequest.product_id", qualifiedByName = "mapIdToProduct")
    @Mapping(target = "id", ignore = true)
    OrderItem requestItemToEntity(OrderItemRequest orderItemRequest, Order order);



    @Mapping(target = "orderItems", expression = "java(new LinkedHashSet<>())")
    @Mapping(target = "orderStatuses", expression = "java(new LinkedHashSet<>())")
    Order requestOrderToEntity(OrderRequest orderRequest);


    UserDto entityUserToDto(User user);

    OrderDTO entityOrderToDto(Order order);
    @Mapping(target = "order", source = "order")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", ignore = true)
    OrderStatus buildOrderStatus(Order order, String status);

    Set<OrderItemDTO> entityOrderItemToDtos(Set<OrderItem> orderItem);
    OrderItemDTO entityOrderItemToDto(OrderItem orderItem);
    ProductDto entityProductToDto(Product product);

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

}
