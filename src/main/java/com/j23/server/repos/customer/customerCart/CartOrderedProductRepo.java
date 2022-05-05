package com.j23.server.repos.customer.customerCart;

import com.j23.server.models.customer.customerCart.CartOrderedProduct;
import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartOrderedProductRepo extends JpaRepository<CartOrderedProduct, String> {

//  @Transactional
//  void deleteAllBy(String productId);

  List<CartOrderedProduct> findAllByProductId(String id);


//    @Modifying
//    @Query("update CartOrderedProduct op set op.quantity = ?1 where op.id = ?2")
//    int updateQuantity(Integer status, Long id);

//  DELETE FROM `ordered_product` WHERE `ordered_product`.`id` = 'NFx9TpdZrmMtDyUoI88dudnAjWb2_satayayam'

}
