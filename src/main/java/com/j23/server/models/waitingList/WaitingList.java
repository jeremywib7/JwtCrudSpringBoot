package com.j23.server.models.waitingList;

import com.j23.server.models.customer.Customer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@Setter
@Getter
@ToString
public class WaitingList {

    @Id
    private Long id;

    @OneToOne
    private Customer customer;


}
