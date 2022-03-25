package com.j23.server.models.customer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Setter
@Getter
@ToString
public class Customer {

    @Id
    private String id;

    private String customerName;
}
