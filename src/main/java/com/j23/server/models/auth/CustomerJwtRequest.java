package com.j23.server.models.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CustomerJwtRequest {
    private String email;
    private String userPassword;
}
