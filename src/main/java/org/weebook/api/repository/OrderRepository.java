package org.weebook.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.dto.TKProductDto;
import org.weebook.api.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {

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


    @Query("""
                select new TKProductDto(p.id, p.name, p.images, sum(oi.quantity), cast(sum(oi.quantity * oi.unitPrice) as bigdecimal))
                from Order o join o.orderItems oi join oi.product p
                where o.status = 'success' and date(o.orderDate) >= :dateMin and date(o.orderDate) <= :dateMax
                and  p.name like :nameProduct
                group by p.id, p.name, p.images
            """)
    List<TKProductDto> byOrder(LocalDate dateMin, LocalDate dateMax, String nameProduct);
}
