package com.j23.server.models.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class CustomerJwtResponse {
    private Customer customer;
    private String jwtToken;
    private String refreshToken;
}
