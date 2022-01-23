package com.j23.server.repos.product;

import com.j23.server.models.product.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    @RestResource(path = "categoryid")
    Page<Product> findByCategoryId(Long id, Pageable pageable);
}
