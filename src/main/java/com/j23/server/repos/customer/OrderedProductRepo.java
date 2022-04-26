package com.j23.server.repos.customer;

import com.j23.server.models.customer.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.OrderedProduct;
import com.j23.server.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderedProductRepo extends JpaRepository<OrderedProduct, String> {

//    List<OrderedProduct> findByCustomerCart(CustomerCart customerCart);

    @Modifying
    @Query("update OrderedProduct op set op.quantity = ?1 where op.id = ?2")
    int updateQuantity(Integer status, Long id);

//  DELETE FROM `ordered_product` WHERE `ordered_product`.`id` = 'NFx9TpdZrmMtDyUoI88dudnAjWb2_satayayam'

}
