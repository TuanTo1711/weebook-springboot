package org.weebook.api.service;

import org.weebook.api.dto.OrderDTO;
import org.weebook.api.dto.OrderDetailDTO;
import org.weebook.api.dto.OrderFeedBackDto;
import org.weebook.api.dto.TkDto;
import org.weebook.api.web.request.*;

import java.util.List;

public interface OrderService {
    OrderDetailDTO sendOrder(OrderRequest orderRequest);

    OrderDetailDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest);

    OrderFeedBackDto orderFeedback(OrderFeedBackRequest orderFeedBackRequest);

    List<OrderDTO> userFindByStatus(FindOrderStatusRequest findOrderStatusRequest);

    List<OrderDTO> adminFindByStatus(FindOrderStatusRequest findOrderStatusRequest);

    TkDto tkByOrder(TkOrderRequest tkOrderRequest);

    List<String> getYearMonth();

    OrderDetailDTO findById(Long id);
}
