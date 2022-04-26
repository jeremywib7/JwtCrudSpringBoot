package com.j23.server.services.product;

import com.j23.server.models.product.Product;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.models.product.ProductImage;
import com.j23.server.models.product.UnassignedProduct;
import com.j23.server.repos.product.ProductCategoryRepository;
import com.j23.server.repos.product.ProductRepository;
import com.j23.server.services.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ProductCategoryService productCategoryService;

  @Autowired
  private ProductCategoryRepository productCategoryRepository;

  public void createTestProduct() {

    // create 2 sample category

    ProductCategory breakfast = new ProductCategory();
    breakfast.setId("breakfast");
    breakfast.setCategoryName("Breakfast");
    breakfast.setCreatedOn(LocalDateTime.now());
    breakfast.setUpdatedOn(LocalDateTime.now());
    productCategoryRepository.save(breakfast);

    ProductCategory dinner = new ProductCategory();
    dinner.setId("dinner");
    dinner.setCategoryName("Dinner");
    dinner.setCreatedOn(LocalDateTime.now());
    dinner.setUpdatedOn(LocalDateTime.now());
    productCategoryRepository.save(dinner);


    // create 3 sample product
    // add default image
    ProductImage defaultImage = new ProductImage();
    List<ProductImage> listImages = new ArrayList<>();
    listImages.add(defaultImage);

    Product satayAyam = new Product();
    satayAyam.setId("satayayam");
    satayAyam.setName("Satay Ayam");
    satayAyam.setTotalCalories(201L);
    satayAyam.setDescription("A sate ayam made with love");
    satayAyam.setDiscount(false);
    satayAyam.setUnitPrice(BigDecimal.valueOf(35000));
    satayAyam.setDiscountedPrice(BigDecimal.valueOf(35000));
    satayAyam.setCreatedOn(LocalDateTime.from(LocalDateTime.now()));
    satayAyam.setUpdatedOn(null);
    satayAyam.setCategory(breakfast);
    satayAyam.setActive(true);
    satayAyam.setImages(listImages);
    productRepository.save(satayAyam);

    Product banana = new Product();
    banana.setId("banana");
    banana.setName("A Banana");
    banana.setTotalCalories(101L);
    banana.setDescription("A banana made with love");
    banana.setDiscount(false);
    banana.setUnitPrice(BigDecimal.valueOf(25000));
    banana.setDiscountedPrice(BigDecimal.valueOf(25000));
    banana.setCreatedOn(LocalDateTime.from(LocalDateTime.now()));
    banana.setUpdatedOn(null);
    banana.setCategory(breakfast);
    banana.setActive(true);
    banana.setImages(listImages);
    productRepository.save(banana);

    Product iceCream = new Product();
    iceCream.setId("icecream");
    iceCream.setName("Ice Cream");
    iceCream.setTotalCalories(101L);
    iceCream.setDescription("A ice cream made with love");
    iceCream.setDiscount(true);
    iceCream.setUnitPrice(BigDecimal.valueOf(10000));
    iceCream.setDiscountedPrice(BigDecimal.valueOf(8000));
    iceCream.setCreatedOn(LocalDateTime.now());
    iceCream.setUpdatedOn(null);
    iceCream.setCategory(dinner);
    iceCream.setActive(false);
    iceCream.setImages(listImages);
    productRepository.save(iceCream);

    // create
  }

  public Product addProduct(Product product) {
    if (productRepository.existsByName(product.getName())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Product name already exists");
    }

    LocalDateTime localDateTime = LocalDateTime.now();
    product.setId(String.valueOf(UUID.randomUUID()));
    product.setCreatedOn(LocalDateTime.from(localDateTime));

    return productRepository.save(product);
  }

  public Product updateProduct(Product product) {
    LocalDateTime localDateTime = LocalDateTime.now();
    product.setUpdatedOn(LocalDateTime.from(localDateTime));

    return productRepository.save(product);
  }

  public void deleteProductById(String id) {
    // delete in database
    productRepository.deleteProductById(id);

    // delete folder image
    imageService.deletePath(id);
  }

  public void deleteSelectedProducts(List<String> id) {
    id.forEach(value -> {
      // delete in database
      productRepository.deleteProductById(value);

      // delete folder image
      imageService.deletePath(value);
    });
  }

  public Page<Product> findAllProduct(Pageable pageable, Long minCalories, Long maxCalories,
                                      BigDecimal minPrice, BigDecimal maxPrice) {

    return productRepository.findAllByTotalCaloriesBetweenAndUnitPriceBetween(minCalories,
      maxCalories, minPrice, maxPrice, pageable);
  }

  public Page<Product> findAllProductByName(String searchKeyword, Pageable pageable) {
    return productRepository.findAllByNameContaining(searchKeyword, pageable);
  }

  public Page<Product> findAllProductForTable(String searchKeyword, int page, int size, String sortedFieldName
    , int order) {

    if (size < 5 || size > 50) {
      size = 10;
    }

    return productRepository.findAllBySearchTable(searchKeyword, PageRequest.of(page, size,
      Sort.by(order == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, sortedFieldName)));
  }

  public Product findProductById(String id) {
    return productRepository.findProductById(id);
  }

  public Optional<Product> findProductByName(String id) {
    return productRepository.findByName(id);
  }

  public Product removeProductInCategory(String productId) {

    if (!productRepository.existsById(productId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product category does not exists");
    }

    Product product = productRepository.findProductById(productId);
    ProductCategory productCategory = productCategoryRepository.findProductCategoryById("akisjasas-asajek-ajsoaks-ejakjenafe");

    product.setCategory(productCategory);
    product.getCategory().setTotalProduct(productCategoryService.getTotalProductOnCategory(
      "akisjasas-asajek-ajsoaks-ejakjenafe"));

    return productRepository.save(product);
  }

  public List<Product> updateUnassignedProduct(List<UnassignedProduct> unassignedProductList) {
    List<Product> productList = new ArrayList<>();

    unassignedProductList.forEach(unassignedProduct -> {
      Product product = productRepository.findProductById(unassignedProduct.getProductId());
      product.setUpdatedOn(LocalDateTime.now());

      ProductCategory productCategory = productCategoryRepository.findProductCategoryById(unassignedProduct.getCategoryId());
      product.setCategory(productCategory);
      productCategory.setUpdatedOn(LocalDateTime.now());
      productRepository.save(product);

      productList.add(product);
    });

    return productList;
  }

  public Iterable<Product> findAllProductNameOnlyByCategory(String categoryId) {
    return productRepository.findAllByCategoryId(categoryId);
  }

  public Page<Product> findAllProductByFilter(String id, Pageable pageable, Long minCalories, Long maxCalories,
                                              BigDecimal minPrice, BigDecimal maxPrice
  ) {
    return productRepository.findAllByCategoryIdAndTotalCaloriesBetweenAndUnitPriceBetween(id, minCalories,
      maxCalories, minPrice, maxPrice, pageable);
  }

}
