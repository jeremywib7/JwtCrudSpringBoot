package com.j23.server.models.customer.customerCart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.j23.server.models.customer.CustomerProfile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @Column(name = "date_updated")
    private LocalDateTime updatedOn;

    @JsonIncludeProperties(value = {"id", "username"})
    @OneToOne()
    @JoinColumn(name = "customer_id")
    private CustomerProfile customerProfile;

    @OneToMany(targetEntity = CartOrderedProduct.class)
    @JoinColumn(name = "cart_id")
    private List<CartOrderedProduct> cartOrderedProduct;

}
