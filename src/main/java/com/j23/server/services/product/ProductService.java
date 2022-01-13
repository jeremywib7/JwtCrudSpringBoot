package com.j23.server.services.product;

import com.j23.server.models.product.Product;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Iterable<Product> findAllProduct() {
        return productRepository.findAll();
    }

}
