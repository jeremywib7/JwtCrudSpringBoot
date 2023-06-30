package com.j23.server.models.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Setter
@Getter
@ToString
public class ProductImage {

    @Id
    @Column(length = 100)
    private String imageName;

}
