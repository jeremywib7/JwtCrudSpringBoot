package com.j23.server.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {
    private String username;
    private String oldPassword;
    private String newPassword;
}
