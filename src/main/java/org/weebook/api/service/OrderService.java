package org.weebook.api.service;

import org.weebook.api.dto.OrderDTO;
import org.weebook.api.web.request.OrderRequest;
import org.weebook.api.web.request.UpdateStatusOrderRequest;

import java.util.List;

public interface OrderService {
    public OrderDTO order(OrderRequest orderRequest);

    public OrderDTO updateStatus(UpdateStatusOrderRequest updateStatusOrderRequest);

    List<OrderDTO> userFindByStatus(Long idUser, String status, Integer page);

    List<OrderDTO> adminFindByStatus(String status, Integer page);
}
