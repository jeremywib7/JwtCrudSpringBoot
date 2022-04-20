package com.j23.server.models.customer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@Setter
@Getter
@ToString
public class OrderHistory {

    @Id
    private String id;

    @ManyToOne
    private CustomerProfile customerProfileDetail;


}
