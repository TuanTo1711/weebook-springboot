package org.weebook.api.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    @EntityGraph(attributePaths = {"images", "reviews", "authors", "category"})
    @Override
    @NonNull
    List<Product> findAll(@NonNull Specification<Product> spec);

    @EntityGraph(attributePaths = {"images", "reviews", "authors", "category"})
    @Override
    @NonNull
    Page<Product> findAll(@NonNull Specification<Product> spec, @NonNull Pageable pageable);

    @Query("""
        select p from Product p join p.orderItems oi
        where CAST(oi.order.orderDate AS localdate) >= :dateMin
            and CAST(oi.order.orderDate AS localdate) <= :dateMax
        group by p
        order by cast(sum(oi.unitPrice*oi.quantity) as bigdecimal) desc
    """)
    List<Product> trend(LocalDate dateMin, LocalDate dateMax, Pageable pageable);

}
