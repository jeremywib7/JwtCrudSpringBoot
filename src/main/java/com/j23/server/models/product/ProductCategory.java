package com.j23.server.models.product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name="product_category")
@Setter
@Getter
@ToString
public class ProductCategory {

    @Id
    @Column(unique = true)
    private String id;

    @Column(nullable = false)
    private String categoryName;

}
