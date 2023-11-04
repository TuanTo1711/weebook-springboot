package org.weebook.api.service;

import org.weebook.api.dto.OrderDTO;
import org.weebook.api.dto.OrderFeedBackDto;
import org.weebook.api.dto.TKProductDto;
import org.weebook.api.web.request.OrderFeedBackRequest;
import org.weebook.api.web.request.OrderRequest;
import org.weebook.api.web.request.UpdateStatusOrderRequest;

import java.util.List;

public interface OrderService {
    OrderDTO order(OrderRequest orderRequest);

    OrderDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest);

    OrderFeedBackDto orderfeedback(OrderFeedBackRequest orderFeedBackRequest);

    List<OrderDTO> userFindByStatus(Long idUser, String status, Integer page);

    List<OrderDTO> adminFindByStatus(String status, Integer page);

    List<TKProductDto> tkByOrder();
}
