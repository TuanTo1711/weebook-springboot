package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weebook.api.entity.OrderFeedback;

public interface OrderFeedBackRepository extends JpaRepository<OrderFeedback, Long> {
}
