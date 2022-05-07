package com.j23.server.models.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Setter
@Getter
@ToString
public class ProductImage {
    @Id
    private String id;

    @Column(length = 100)
    private String imageName;

}
