package org.weebook.api.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.*;
import org.weebook.api.projection.OrderStatusProjection;
import org.weebook.api.service.OrderService;
import org.weebook.api.web.request.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderDetailDTO order(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.sendOrder(orderRequest);
    }

    @PostMapping("/update-status")
    public OrderDetailDTO updateStatus(@RequestBody UpdateStatusOrderRequest updateStatusOrderRequest) {
        return orderService.updateStatus(updateStatusOrderRequest);
    }

    @PostMapping("/feedback")
    public OrderFeedBackDto feedback(@Valid @RequestBody OrderFeedBackRequest orderFeedBackRequest) {
        return orderService.orderFeedback(orderFeedBackRequest);
    }

    @GetMapping("findBy")
    public OrderDetailDTO orderDetailDTO(@Param("id") Long id){
        return orderService.findById(id);
    }

    @PostMapping("/user-find-status")
    public List<OrderStatusProjection> userFindByStatus(@RequestBody FindOrderStatusRequest status) {
        return orderService.userFindByStatus(status);
    }

    @GetMapping("/admin-find-status")
    public List<OrderDTO> adminFindByStatus(@RequestBody FindOrderStatusRequest status) {
        return orderService.adminFindByStatus(status);
    }

    @PostMapping("/tk-by-order")
    public TkDto tkByOrder(@RequestBody TkOrderRequest tkOrderRequest) {
        return orderService.tkByOrder(tkOrderRequest);
    }

    @GetMapping("get-by-year")
    public List<String> getYearMonth() {
        return orderService.getYearMonth();
    }

    @PostMapping("/trend")
    public List<ProductInfo> getTrend(@RequestBody TrendProductRequest trendProductRequest){
        return orderService.trend(trendProductRequest);
    }
}
