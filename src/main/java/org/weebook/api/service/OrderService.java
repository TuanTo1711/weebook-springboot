package org.weebook.api.service;

import org.weebook.api.dto.*;
import org.weebook.api.projection.OrderStatusProjection;
import org.weebook.api.web.request.*;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderDetailDTO sendOrder(OrderRequest orderRequest);

    OrderDetailDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest);

    OrderFeedBackDto orderFeedback(OrderFeedBackRequest orderFeedBackRequest);

    List<OrderStatusProjection> userFindByStatus(String status, PagingRequest pagingRequest);

    List<OrderDTO> adminFindByStatus(String status, PagingRequest pagingRequest);

    TkDto tkByOrder(String yearMonth, String nameProduct, PagingRequest pagingRequest);

    List<String> getYearMonth();

    OrderDetailDTO findById(Long id);

    List<ProductInfo> trend(LocalDate dateMin, LocalDate dateMax, PagingRequest pagingRequest);

    void addOrder();
}
