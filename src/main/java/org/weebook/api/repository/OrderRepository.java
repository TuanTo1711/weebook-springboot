package org.weebook.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.dto.TKProductDto;
import org.weebook.api.entity.Order;
import org.weebook.api.entity.User;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("""
        select o from Order o
        where o.user.id = :user and o.status = :status
    """)
    Page<Order> userFindByStatus(Long user, String status, Pageable pageable);

    @Query("""
        select o from Order o
        where o.status = :status
    """)
    Page<Order> adminFindByStatus(String status, Pageable pageable);



}
