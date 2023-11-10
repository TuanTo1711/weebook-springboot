package org.weebook.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.OrderStatus;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    @Query("""
    SELECT o FROM OrderStatus o
    WHERE o.order.user.id = :idUser and o.order.status = :status
    and o.status = :status
    """)
    <T> List<T> userGetAllStatus(String status, Long idUser, Class<T> classType, Pageable pageable);

    @Query("""
    SELECT o FROM OrderStatus o
    WHERE o.order.user.id = :idUser
    order by o.id desc
    """)
    <T> List<T> userGetAllStatus(Long idUser, Class<T> classType, Pageable pageable);
}
