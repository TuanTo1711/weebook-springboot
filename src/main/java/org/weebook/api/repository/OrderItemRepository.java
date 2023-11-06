package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weebook.api.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
