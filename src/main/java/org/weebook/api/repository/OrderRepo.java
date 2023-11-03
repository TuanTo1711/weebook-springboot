package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weebook.api.entity.Order;

public interface OrderRepo extends JpaRepository<Order,Long> {
}
