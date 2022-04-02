package com.j23.server.services.product;

import com.j23.server.exception.UserNotFoundException;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.repos.product.ProductCategoryRepository;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addUnassignedCategory() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId("akisjasas-asajek-ajsoaks-ejakjenafe");
        productCategory.setCategoryName("Unassigned");
        productCategory.setCreatedOn(LocalDateTime.now());
        productCategory.setUpdatedOn(LocalDateTime.now());
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

        productCategory.setUpdatedOn(LocalDateTime.from(LocalDateTime.now()));
        productCategory.setTotalProduct(productRepository.countAllByCategoryId(productCategory.getId()));
        productCategory.setProducts(getAllProductOnCategory(productCategory.getId()));
        productCategoryRepository.save(productCategory);

        return productCategory;
    }

    public void deleteProductCategory(String id) {
        productCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Category name not found"));

        try {
            productCategoryRepository.deleteProductCategoryById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please change or remove product in this category " +
                    "before deleting");
        }
    }

    public Integer getTotalProductOnCategory(String categoryId) {
        return productRepository.countAllByCategoryId(categoryId);
    }

    public List<Product> getAllProductOnCategory(String categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    public List<ProductCategory> findAllProductCategory() {
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();

        productCategoryList.forEach(productCategory -> {
            productCategory.setTotalProduct(getTotalProductOnCategory(productCategory.getId()));
            productCategory.setProducts(getAllProductOnCategory(productCategory.getId()));

            productCategoryRepository.save(productCategory);
        });

        return productCategoryList;
    }

}
