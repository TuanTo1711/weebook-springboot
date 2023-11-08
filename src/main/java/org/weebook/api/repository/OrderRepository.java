package org.weebook.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("""
                select o from Order o
                where o.user.id = :user and o.status = :status
            """)
    List<Order> userFindByStatus(Long user, String status, Pageable pageable);

    @Query("""
                select o from Order o
                where o.status = :status
            """)
    List<Order> adminFindByStatus(String status, Pageable pageable);

    @Query("""
            SELECT DISTINCT
            concat(CAST(YEAR(o.orderDate) AS String), '-',
                   (CASE WHEN MONTH(o.orderDate) < 10 THEN '0' || CAST(MONTH(o.orderDate) AS String)\s
                   ELSE CAST(MONTH(o.orderDate) AS String) END)) AS YearMonth
            FROM Order o WHERE o.status = 'success'
            """)
    List<String> findAllMonthYear();

}
