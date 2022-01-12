package com.j23.server.repos.product;

import com.j23.server.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository <Product, Long> {

}
