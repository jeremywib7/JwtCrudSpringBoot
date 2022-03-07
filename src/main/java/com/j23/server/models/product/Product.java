package com.j23.server.models.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "product")
@Setter
@Getter
@ToString
public class Product {

    @Id
    @Column(unique = true)
    @JsonView(Views.ProductNameViews.class)
    private String id;

    @Column(length = 25)
    @JsonView(Views.ProductNameViews.class)
    private String name;

    private Long totalCalories;

    private boolean discount;

    @Column(length = 100)
    private String description;

    @Column(precision = 13, scale = 2, name = "unit_price")
    private BigDecimal unitPrice;

    @Column(precision = 13, scale = 2, name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "image_url", length = 30)
    private String imageUrl;

    private boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "date_created")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "last_updated")
    private LocalDateTime updatedOn;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(targetEntity = ImageArray.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_fk", referencedColumnName = "id")
    private List<ImageArray> images;

}
