package com.j23.server.services.product;

import com.j23.server.models.product.ProductCategory;
import com.j23.server.repos.product.ProductCategoryRepository;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addUnasignedCategory() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId("akisjasas-asajek-ajsoaks-ejakjenafe");
        productCategory.setCategoryName("Unassigned");
        productCategoryRepository.save(productCategory);
    }

    public ProductCategory addProductCategory(ProductCategory productCategory) {

        if (productCategoryRepository.existsByCategoryName(productCategory.getCategoryName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product category already exists");
        }

        productCategory.setId(String.valueOf(UUID.randomUUID()));

        LocalDateTime localDateTime = LocalDateTime.now();
        productCategory.setCreatedOn(LocalDateTime.from(localDateTime));

        productCategory.setTotalProduct(0);

        return productCategoryRepository.save(productCategory);
    }

    public ProductCategory updateProductCategory(ProductCategory productCategory) {

        if (productCategoryRepository.existsByCategoryNameAndIdIsNotLike(productCategory.getCategoryName(),
                productCategory.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product category already exists");

        }

        LocalDateTime localDateTime = LocalDateTime.now();
        productCategory.setUpdatedOn(LocalDateTime.from(localDateTime));
        productCategory.setTotalProduct(productRepository.countAllByCategoryId(productCategory.getId()));

        return productCategoryRepository.save(productCategory);
    }

    public Integer getTotalProductOnCategory(String categoryId) {
        return productRepository.countAllByCategoryId(categoryId);
    }

    public void deleteProductCategoryById(String id) {
        productCategoryRepository.deleteProductCategoryById(id);
    }

    public Iterable<ProductCategory> findAllProductCategory() {
        return productCategoryRepository.findAll();
    }
}
