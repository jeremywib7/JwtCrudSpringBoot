package com.j23.server.models.product.ProductSteps;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.models.product.Views;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "product")
@Setter
@Getter
@ToString
public class Product {

//     Product Details

    class CustomException {
        private Date timestamp;
        private Message message; //can even split message further.
        private String details;
    }

    static class Message {
        private int id;
        private String category;
        private String comment;
    }

    @Id
    @Column(unique = true)
    @JsonView(Views.ProductNameViews.class)
    private String id;

    @OneToOne()
    private ProductDetail productDetail;

    @OneToOne()
    private ProductPricing productPricing;

    @OneToMany(targetEntity = ProductImage.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_fk", referencedColumnName = "id")
    private List<ProductImage> images;

}
