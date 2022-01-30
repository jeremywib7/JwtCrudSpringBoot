package com.j23.server.models.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Setter
@Getter
@ToString
public class ImageArray {

    @Id
    private String id;

    @Column(length = 30)
    private String imageName;

    @PrePersist
    protected void onCreate() {
        setId(String.valueOf(UUID.randomUUID()));
    }

}
