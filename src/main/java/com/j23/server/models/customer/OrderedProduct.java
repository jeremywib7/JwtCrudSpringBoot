package com.j23.server.models.customer;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.Views;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIncludeProperties(value = {"name","unitPrice","discount","discountedPrice"})
    @OneToOne(targetEntity = Product.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Min(1)
    @Max(100000)
    @Column(name = "quantity")
    private Integer quantity;
}
