package org.weebook.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.weebook.api.entity.Category;
import org.weebook.api.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product ,Long> , JpaSpecificationExecutor<Product> {

}
