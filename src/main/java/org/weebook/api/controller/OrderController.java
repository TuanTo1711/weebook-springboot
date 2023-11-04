package org.weebook.api.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.OrderDTO;
import org.weebook.api.dto.OrderFeedBackDto;
import org.weebook.api.dto.TKProductDto;
import org.weebook.api.service.OrderService;
import org.weebook.api.web.request.OrderFeedBackRequest;
import org.weebook.api.web.request.OrderRequest;
import org.weebook.api.web.request.UpdateStatusOrderRequest;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
     final OrderService orderService;
    @PostMapping
    public OrderDTO order(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.order(orderRequest);
    }

    @PostMapping("/update/status")
    public OrderDTO updateStatus(@RequestBody UpdateStatusOrderRequest updateStatusOrderRequest){
        return orderService.updateStatus(updateStatusOrderRequest);
    }

    @PostMapping("/feedback")
    public OrderFeedBackDto feedback(@Valid @RequestBody OrderFeedBackRequest orderFeedBackRequest) {
        return orderService.orderfeedback(orderFeedBackRequest);
    }

    @GetMapping("/user/find/status")
    public List<OrderDTO> userFindByStatus(@Param("idUser") Long idUser, @Param("status") String status, @Param("page") Integer page){
        return orderService.userFindByStatus(idUser, status, page);
    }

    @GetMapping("/admin/find/status")
    public List<OrderDTO> userFindByStatus(@Param("status") String status, @Param("page") Integer page){
        return orderService.adminFindByStatus(status, page);
    }


}
