package com.j23.server.models.product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@ToString
public class UnassignedProduct {

    private String productName;
    private String categoryName;
}
