package org.weebook.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.Genre;
import org.weebook.api.entity.Product;

import java.util.List;

public interface genres extends JpaRepository<Genre,Long>, JpaSpecificationExecutor<Genre> {
    Page<Genre> findAll(Specification specification, Pageable pageable);

    @Query("""
        select g from Genre g join g.products p
        where p.category.id in :objects
        group by g
    """)
    List<Genre> listGenresByCategoryName(List<Object>  objects);

}
