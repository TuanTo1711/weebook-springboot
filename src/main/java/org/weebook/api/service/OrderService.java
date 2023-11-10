package org.weebook.api.service;

import org.weebook.api.dto.*;
import org.weebook.api.projection.OrderStatusProjection;
import org.weebook.api.web.request.*;

import java.util.List;

public interface OrderService {
    OrderDetailDTO sendOrder(OrderRequest orderRequest);

    OrderDetailDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest);

    OrderFeedBackDto orderFeedback(OrderFeedBackRequest orderFeedBackRequest);

    List<OrderStatusProjection> userFindByStatus(FindOrderStatusRequest findOrderStatusRequest);

    List<OrderDTO> adminFindByStatus(FindOrderStatusRequest findOrderStatusRequest);

    TkDto tkByOrder(TkOrderRequest tkOrderRequest);

    List<String> getYearMonth();

    OrderDetailDTO findById(Long id);

    List<ProductInfo> trend(TrendProductRequest trendProductRequest);
}
