package com.j23.server.request.auth;

import com.j23.server.models.auth.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String userFirstName;
    @NotNull
    private String userLastName;
    private String imageUrl;
    private String userPassword;
    private String gender;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private LocalDate dateJoined;
    private String phoneNumber;
    private String address;
    private String bankAccount;
    private Role role;
}
