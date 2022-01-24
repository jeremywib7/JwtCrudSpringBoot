package com.j23.server.models.waitingList;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="waiting_list")
@Setter
@Getter
@ToString
public class WaitingList {

    @Id
    private Long id;

    private String duration;
    private boolean completed;


}
