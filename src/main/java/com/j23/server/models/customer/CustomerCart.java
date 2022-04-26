package com.j23.server.models.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.j23.server.models.auth.User;
import com.j23.server.models.product.ProductImage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Setter
@Getter
@ToString
public class CustomerCart {

    @Id
    @Column(name = "cart_id")
    private String id;

    @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @JsonIncludeProperties(value = {"id", "username"})
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private CustomerProfile customerProfile;

    @OneToMany(targetEntity = ProductImage.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private List<OrderedProduct> orderedProduct;

}
