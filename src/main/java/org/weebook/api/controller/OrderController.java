package org.weebook.api.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.*;
import org.weebook.api.projection.OrderStatusProjection;
import org.weebook.api.service.OrderService;
import org.weebook.api.web.request.*;

import java.time.LocalDate;
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

    @GetMapping("/user-find-status")
    public List<OrderStatusProjection> userFindByStatus(String status, @ModelAttribute PagingRequest pagingRequest) {
        return orderService.userFindByStatus(status, pagingRequest);
    }

    @GetMapping("/admin-find-status")
    public List<OrderDTO> adminFindByStatus(String status, @ModelAttribute PagingRequest pagingRequest) {
        return orderService.adminFindByStatus(status, pagingRequest);
    }

    @GetMapping("/tk-by-order")
    public TkDto tkByOrder(String yearMonth, String nameProduct, PagingRequest pagingRequest) {
        return orderService.tkByOrder(yearMonth, nameProduct, pagingRequest);
    }

    @GetMapping("get-by-year")
    public List<String> getYearMonth() {
        return orderService.getYearMonth();
    }

    @GetMapping("/trend")
    public List<ProductInfo> getTrend(LocalDate dateMin, LocalDate dateMax, PagingRequest pagingRequest){
        return orderService.trend(dateMin,dateMax, pagingRequest);
    }

}
