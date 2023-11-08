package org.weebook.api.service;

import org.weebook.api.dto.OrderDTO;
import org.weebook.api.dto.OrderFeedBackDto;
import org.weebook.api.dto.TkDto;
import org.weebook.api.web.request.OrderFeedBackRequest;
import org.weebook.api.web.request.OrderRequest;
import org.weebook.api.web.request.TkOrderRequest;
import org.weebook.api.web.request.UpdateStatusOrderRequest;

import java.util.List;

public interface OrderService {
    OrderDTO sendOrder(OrderRequest orderRequest);

    OrderDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest);

    OrderFeedBackDto orderFeedback(OrderFeedBackRequest orderFeedBackRequest);

    List<OrderDTO> userFindByStatus(Long idUser, String status, Integer page);

    List<OrderDTO> adminFindByStatus(String status, Integer page);

    TkDto tkByOrder(TkOrderRequest tkOrderRequest);

    List<String> getYearMonth();
}
