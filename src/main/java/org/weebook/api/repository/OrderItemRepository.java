package org.weebook.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.weebook.api.dto.TKProductDto;
import org.weebook.api.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
                Select new TKProductDto (o.product.id, o.product.name,
                    o.product.thumbnail, sum(o.quantity), cast(sum(o.quantity * o.unitPrice) as BIGDECIMAL ))
                from OrderItem o
                where o.product.name like :name and o.order.status = 'success' and
                      month(o.order.orderDate) = :month and
                      year(o.order.orderDate) = :year
                group by o.product.id, o.product.name,o.product.thumbnail
            """)
    List<TKProductDto> thongke(Integer month, Integer year, @Param("name") String name, Pageable pageable);

    @Query("""
                Select new TKProductDto (o.product.id, o.product.name,
                    o.product.thumbnail, sum(o.quantity), cast(sum(o.quantity * o.unitPrice) as BIGDECIMAL ))
                from OrderItem o
                where o.product.name like :name and o.order.status = 'success'
                group by o.product.id, o.product.name,o.product.thumbnail
            """)
    List<TKProductDto> thongke(@Param("name") String name, Pageable pageable);

    @Query("""
                Select cast(sum(o.quantity * o.unitPrice) as BIGDECIMAL)
                from OrderItem o
                where o.product.name like :name and o.order.status = 'success'
                group by o.product.id, o.product.name,o.product.thumbnail
            """)
    BigDecimal thongketotal(@Param("name") String name);

    @Query("""
                Select cast(sum(o.quantity * o.unitPrice) as BIGDECIMAL)
                from OrderItem o
                where o.product.name like :name and o.order.status = 'success' and
                      month(o.order.orderDate) = :month and
                      year(o.order.orderDate) = :year
                group by o.product.id, o.product.name,o.product.thumbnail
            """)
    BigDecimal thongketotal(Integer month, Integer year, @Param("name") String name);
}
