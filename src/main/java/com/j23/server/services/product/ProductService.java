package com.j23.server.services.product;

import com.google.firebase.auth.FirebaseAuthException;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.models.product.ProductImage;
import com.j23.server.models.product.UnassignedProduct;
import com.j23.server.repos.customer.customerCart.CartOrderedProductRepo;
import com.j23.server.repos.customer.customerOrder.HistoryProductOrderRepo;
import com.j23.server.repos.product.ProductCategoryRepository;
import com.j23.server.repos.product.ProductRepository;
import com.j23.server.services.SeederService;
import com.j23.server.services.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.j23.server.util.AppsConfig.PRODUCT_FOLDER;

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

    @Autowired
    private HistoryProductOrderRepo historyProductOrderRepo;

    @Autowired
    private CartOrderedProductRepo cartOrderedProductRepo;

    @Autowired
    private SeederService seederService;

    public void createTestProduct() throws FirebaseAuthException {

        // create 2 sample category

        ProductCategory breakfast = new ProductCategory();
        breakfast.setId("breakfast");
        breakfast.setCategoryName("Breakfast");
        breakfast.setCreatedOn(LocalDateTime.now());
        breakfast.setUpdatedOn(LocalDateTime.now());
        productCategoryRepository.save(breakfast);

        ProductCategory fastfood = new ProductCategory();
        fastfood.setId("fastfood");
        fastfood.setCategoryName("Fast Food");
        fastfood.setCreatedOn(LocalDateTime.now());
        fastfood.setUpdatedOn(LocalDateTime.now());
        productCategoryRepository.save(fastfood);

        ProductCategory dinner = new ProductCategory();
        dinner.setId("dinner");
        dinner.setCategoryName("Dinner");
        dinner.setCreatedOn(LocalDateTime.now());
        dinner.setUpdatedOn(LocalDateTime.now());
        productCategoryRepository.save(dinner);

        ProductCategory healthy = new ProductCategory();
        healthy.setId("healthy");
        healthy.setCategoryName("Healthy");
        healthy.setCreatedOn(LocalDateTime.now());
        healthy.setUpdatedOn(LocalDateTime.now());
        productCategoryRepository.save(healthy);

        ProductCategory juice = new ProductCategory();
        juice.setId("juice");
        juice.setCategoryName("Juice");
        juice.setCreatedOn(LocalDateTime.now());
        juice.setUpdatedOn(LocalDateTime.now());
        productCategoryRepository.save(juice);


        // create 5 sample product
        // add default image

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

        ProductImage satayAyam_0 = new ProductImage();
        satayAyam_0.setImageName("satayayam_0.jpg");

        ProductImage satayAyam_1 = new ProductImage();
        satayAyam_1.setImageName("satayayam_1.jpg");

        List<ProductImage> listImages = new ArrayList<>();
        listImages.add(satayAyam_0);
        listImages.add(satayAyam_1);
        satayAyam.setImages(listImages);

        productRepository.save(satayAyam);


        Product banana = new Product();
        banana.setId("banana");
        banana.setName("A Banana");
        banana.setTotalCalories(101L);
        banana.setDescription("A banana with very awesome taste");
        banana.setDiscount(false);
        banana.setUnitPrice(BigDecimal.valueOf(25000));
        banana.setDiscountedPrice(BigDecimal.valueOf(25000));
        banana.setCreatedOn(LocalDateTime.from(LocalDateTime.now()));
        banana.setUpdatedOn(null);
        banana.setCategory(breakfast);
        banana.setActive(true);

        ProductImage aBanana_1 = new ProductImage();
        aBanana_1.setImageName("banana_0.jpeg");

        List<ProductImage> listImagesBanana = new ArrayList<>();
        listImagesBanana.add(aBanana_1);
        banana.setImages(listImagesBanana);
        productRepository.save(banana);


        Product caesarteaser = new Product();
        caesarteaser.setId("caesar teaser");
        caesarteaser.setName("Caesar Teaser");
        caesarteaser.setTotalCalories(71L);
        caesarteaser.setDescription("A salad wrapped in taco with fresh vegetables and meat");
        caesarteaser.setDiscount(false);
        caesarteaser.setUnitPrice(BigDecimal.valueOf(55000));
        caesarteaser.setDiscountedPrice(BigDecimal.valueOf(55000));
        caesarteaser.setCreatedOn(LocalDateTime.from(LocalDateTime.now()));
        caesarteaser.setUpdatedOn(null);
        caesarteaser.setCategory(healthy);
        caesarteaser.setActive(true);

        ProductImage caesarteaser_0 = new ProductImage();
        caesarteaser_0.setImageName("caesar teaser_0.jpg");

        List<ProductImage> listCaesarImages = new ArrayList<>();
        listCaesarImages.add(caesarteaser_0);
        caesarteaser.setImages(listCaesarImages);
        productRepository.save(caesarteaser);


        Product burger = new Product();
        burger.setId("burger");
        burger.setName("Burger");
        burger.setTotalCalories(241L);
        burger.setDescription("A burger made with special ingredients and premium australian beef");
        burger.setDiscount(true);
        burger.setUnitPrice(BigDecimal.valueOf(50000));
        burger.setDiscountedPrice(BigDecimal.valueOf(40000));
        burger.setCreatedOn(LocalDateTime.from(LocalDateTime.now()));
        burger.setUpdatedOn(null);
        burger.setCategory(dinner);
        burger.setActive(true);

        ProductImage burgerImage_0 = new ProductImage();
        burgerImage_0.setImageName("burger_0.png");

        List<ProductImage> listBurgerImages = new ArrayList<>();
        listBurgerImages.add(burgerImage_0);
        burger.setImages(listBurgerImages);
        productRepository.save(burger);


        Product iceCream = new Product();
        iceCream.setId("icecream");
        iceCream.setName("Ice Cream");
        iceCream.setTotalCalories(101L);
        iceCream.setDescription("A ice cream with secret sauces");
        iceCream.setDiscount(true);
        iceCream.setUnitPrice(BigDecimal.valueOf(10000));
        iceCream.setDiscountedPrice(BigDecimal.valueOf(8000));
        iceCream.setCreatedOn(LocalDateTime.now());
        iceCream.setUpdatedOn(null);
        iceCream.setCategory(dinner);
        iceCream.setActive(true);

        ProductImage iceCream_1 = new ProductImage();
        iceCream_1.setImageName("icecream_0.jpeg");

        List<ProductImage> listImagesIceCream = new ArrayList<>();
        listImagesIceCream.add(iceCream_1);
        iceCream.setImages(listImagesIceCream);
        productRepository.save(iceCream);


        Product orange_juice = new Product();
        orange_juice.setId("orangejuice");
        orange_juice.setName("Orange Juice");
        orange_juice.setTotalCalories(51L);
        orange_juice.setDescription("Orange juice made with premium orange fruit");
        orange_juice.setDiscount(false);
        orange_juice.setUnitPrice(BigDecimal.valueOf(10000));
        orange_juice.setDiscountedPrice(BigDecimal.valueOf(10000));
        orange_juice.setCreatedOn(LocalDateTime.now());
        orange_juice.setUpdatedOn(null);
        orange_juice.setCategory(juice);
        orange_juice.setActive(true);

        ProductImage orange_juice_0 = new ProductImage();
        orange_juice_0.setImageName("orange juice_0.jpg");
        ProductImage orange_juice_1 = new ProductImage();
        orange_juice_1.setImageName("orange juice_1.jpg");

        List<ProductImage> listOrangeJuiceImg = new ArrayList<>();
        listOrangeJuiceImg.add(orange_juice_0);
        listOrangeJuiceImg.add(orange_juice_1);
        orange_juice.setImages(listOrangeJuiceImg);
        productRepository.save(orange_juice);


        Product wagyu_steak = new Product();
        wagyu_steak.setId("wagyusteak");
        wagyu_steak.setName("Wagyu Steak");
        wagyu_steak.setTotalCalories(121L);
        wagyu_steak.setDescription("A wagyu steak made with premium quality");
        wagyu_steak.setDiscount(false);
        wagyu_steak.setUnitPrice(BigDecimal.valueOf(500000));
        wagyu_steak.setDiscountedPrice(BigDecimal.valueOf(500000));
        wagyu_steak.setCreatedOn(LocalDateTime.now());
        wagyu_steak.setUpdatedOn(null);
        wagyu_steak.setCategory(breakfast);
        wagyu_steak.setActive(true);

        ProductImage wagyusteak_0 = new ProductImage();
        wagyusteak_0.setImageName("wagyusteak_0.jpg");
        ProductImage wagyusteak_1 = new ProductImage();
        wagyusteak_1.setImageName("wagyusteak_1.jpg");

        List<ProductImage> listWagyuSteakImages = new ArrayList<>();
        listWagyuSteakImages.add(wagyusteak_0);
        listWagyuSteakImages.add(wagyusteak_1);
        wagyu_steak.setImages(listWagyuSteakImages);
        productRepository.save(wagyu_steak);

//        seederService.seedCustomerInFirebase();
        seederService.seedOrder();

    }

    public Product addProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product name already exists");
        }

        if (product.getId().isEmpty() || product.getId() == null || Objects.equals(product.getId(), "")) {
            product.setId(String.valueOf(UUID.randomUUID()));
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        product.setCreatedOn(LocalDateTime.from(localDateTime));

        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {

        LocalDateTime localDateTime = LocalDateTime.now();
        product.setUpdatedOn(LocalDateTime.from(localDateTime));

        return productRepository.save(product);
    }

    public void deleteProductById(String productId) {

        // delete product in database
        productRepository.deleteProductById(productId);

        // delete folder image
        imageService.resetAllFilesInDirectory(PRODUCT_FOLDER, productId);
    }

    public void deleteSelectedProducts(List<String> ids) {
        ids.forEach(id -> {
            // delete in database
            productRepository.deleteProductById(id);

            // delete folder image
            imageService.resetAllFilesInDirectory(PRODUCT_FOLDER, id);
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
            , int order, String categoryName) {
        System.out.println("The cat : " + categoryName);
        if (categoryName != null) {
            ProductCategory productCategory = productCategoryRepository.findByCategoryNameAndCategoryNameIsNot(categoryName, "Unassigned");
            return productRepository.findAllByCategory(productCategory, PageRequest.of(page, size,
                    Sort.by(order == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, sortedFieldName)));
        }

        if (size < 5 || size > 50) {
            size = 10;
        }

        return productRepository.findAllBySearchTableAndCategoryIdIsNot(searchKeyword, PageRequest.of(page, size,
                Sort.by(order == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, sortedFieldName)), "akisjasas-asajek-ajsoaks-ejakjenafe");
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

    public Long getTotalProducts() {
        return productRepository.count();
    }

}
