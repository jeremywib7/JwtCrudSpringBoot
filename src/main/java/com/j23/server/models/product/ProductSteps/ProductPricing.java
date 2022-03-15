package com.j23.server.models.product.ProductSteps;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@Setter
@Getter
@ToString
public class ProductPricing {

    @Id
    private boolean discount;

    @Column(precision = 13, scale = 2, name = "unit_price")
    private BigDecimal unitPrice;

    @Column(precision = 13, scale = 2, name = "discounted_price")
    private BigDecimal discountedPrice;
}
