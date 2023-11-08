package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weebook.api.entity.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
