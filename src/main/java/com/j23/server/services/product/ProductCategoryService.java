package com.j23.server.services.product;

import com.j23.server.models.primengDropdown.PrimengDropdown;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.models.product.UnassignedProduct;
import com.j23.server.repos.product.ProductCategoryRepository;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

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

  public List<ProductCategory> updateUnassignedProductCategory(List<UnassignedProduct> unassignedProducts) {
    unassignedProducts.forEach(unassignedProduct -> {
      ProductCategory productCategory = productCategoryRepository.findProductCategoryById(unassignedProduct.getCategoryId());
      productCategory.setUpdatedOn(LocalDateTime.now());

      Product product = productRepository.findProductById(unassignedProduct.getProductId());
      product.setCategory(productCategory);

      productRepository.save(product);
    });

    return findAllProductCategory();
  }

  public void deleteProductCategory(List<String> productIdList, String productCategoryId) {
    ProductCategory productCategory = productCategoryRepository.findProductCategoryById("akisjasas-asajek-ajsoaks-ejakjenafe");

    // set product id category to unassigned before deleting category
    if (productIdList != null) {
      productIdList.forEach(value -> {
        Product product = productRepository.findProductById(value);
        product.setCategory(productCategory);
        productRepository.save(product);
      });
    }

    // delete product category
    productCategoryRepository.deleteProductCategoryById(productCategoryId);

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

  public List<PrimengDropdown> getAllProductCategoryInDropdown() {
    List<PrimengDropdown> primengDropdowns = new ArrayList<>();

    List<ProductCategory> productCategoryList = productCategoryRepository.findAllByCategoryNameIsNot("Unassigned");

    productCategoryList.forEach(productCategory -> {
      PrimengDropdown primengDropdown = new PrimengDropdown();
      primengDropdown.setLabel(productCategory.getCategoryName());
      primengDropdown.setValue(productCategory.getId());

      primengDropdowns.add(primengDropdown);
    });

    return primengDropdowns;
  }

}
