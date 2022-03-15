package com.j23.server.services.product;

import com.j23.server.models.product.ProductSteps.Product;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.models.product.UnassignedProduct;
import com.j23.server.repos.product.ProductCategoryRepository;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public Product addProduct(Product product) {
        LocalDateTime localDateTime = LocalDateTime.now();
        product.getProductDetail().setCreatedOn(LocalDateTime.from(localDateTime));

        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        LocalDateTime localDateTime = LocalDateTime.now();
        product.getProductDetail().setUpdatedOn(LocalDateTime.from(localDateTime));

        return productRepository.save(product);
    }

    public void deleteProductById(String id) {
        productRepository.deleteProductById(id);
    }

    public Page<Product> findAllProduct(Pageable pageable, Long minCalories, Long maxCalories,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAllByProductDetailTotalCaloriesBetweenAndProductPricingUnitPriceBetween(minCalories,
                maxCalories, minPrice, maxPrice, pageable);
    }

    public Page<Product> findAllProductByName(String searchKeyword, Pageable pageable) {
        return productRepository.findAllByProductDetailNameContaining(searchKeyword, pageable);
    }

    public Page<Product> findAllProductForTable(String searchKeyword, int page, int size, String sortedFieldName
            , int order) {

        return productRepository.findAllBySearchTable(searchKeyword, PageRequest.of(page, size,
                Sort.by(order == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, sortedFieldName)));
    }

    public Optional<Product> findProductById(String id) {
        return productRepository.findById(id);
    }

    public Product removeProductInCategory(String productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product category does not exists");
        }

        Product product = productRepository.findProductById(productId);
        ProductCategory productCategory = productCategoryRepository.findProductCategoryById("akisjasas-asajek-ajsoaks-ejakjenafe");
        product.getProductDetail().setCategory(productCategory);

        return productRepository.save(product);
    }

    public List<Product> updateUnassignedProduct(List<UnassignedProduct> unassignedProductList) {
        List<Product> productList = new ArrayList<>();

        unassignedProductList.forEach(unassignedProduct -> {
            Product product = productRepository.findProductById(unassignedProduct.getProductId());
            product.getProductDetail().setUpdatedOn(LocalDateTime.now());

            ProductCategory productCategory = productCategoryRepository.findProductCategoryById(unassignedProduct.getCategoryId());
            product.getProductDetail().setCategory(productCategory);
            productCategory.setUpdatedOn(LocalDateTime.now());
            productRepository.save(product);

            productList.add(product);
        });

        return productList;
    }

    public Iterable<Product> findAllProductNameOnlyByCategory(String categoryId) {
        return productRepository.findAllByProductDetailCategoryId(categoryId);
    }

    public Page<Product> findAllProductByFilter(String id, Pageable pageable, Long minCalories, Long maxCalories,
                                                BigDecimal minPrice, BigDecimal maxPrice
    ) {
        return productRepository.findAllByProductDetailCategoryIdAndProductDetailTotalCaloriesBetweenAndProductPricingUnitPriceBetween(id, minCalories,
                maxCalories, minPrice, maxPrice, pageable);
    }

}
