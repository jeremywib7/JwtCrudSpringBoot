package com.j23.server.models.product;

import lombok.*;

import javax.persistence.*;

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
