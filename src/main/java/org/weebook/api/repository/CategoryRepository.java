package org.weebook.api.repository;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.weebook.api.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"children"})
    List<Category> findAll();

    @EntityGraph(attributePaths = {"children"})
    @NonNull
    List<Category> findAll(@NonNull Specification<Category> spec);

    @Query(nativeQuery = true, value = "select * from withrecursive(:name, :parent)")
    List<Category> findWithRecursive(String name, String parent);
}
