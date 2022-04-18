package com.j23.server.models.customerOrder;

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
public class OrderHistory {

    @Id
    private String id;
}
