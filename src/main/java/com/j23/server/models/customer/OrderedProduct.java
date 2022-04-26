package com.j23.server.models.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.j23.server.models.product.Product;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Data
@Setter
@Getter
@ToString
public class OrderedProduct {

    @Id
    private String id;

    @JsonIncludeProperties(value = {"id", "name", "unitPrice", "discount", "discountedPrice"})
    @OneToOne(targetEntity = Product.class, cascade = CascadeType.ALL)
    private Product product;

    @Min(1)
    @Max(100000)
    @Column(name = "quantity")
    private Integer quantity;

//    @JsonIgnore
//    @OneToOne(targetEntity = CustomerCart.class, cascade = CascadeType.ALL)
//    private CustomerCart customerCart;

//    @JsonIgnore
//    @OneToOne(targetEntity = CustomerCart.class, cascade = CascadeType.ALL)
//    private CustomerCart customerCart;

}
