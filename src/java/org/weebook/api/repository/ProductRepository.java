package org.weebook.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.weebook.api.entity.Product;

public interface ProductRepository extends JpaRepository<Product ,Long> , JpaSpecificationExecutor<Product> {



}
