package com.j23.server.services.auth;

import com.j23.server.controllers.exception.UserNotFoundException;
import com.j23.server.models.auth.ChangePassword;
import com.j23.server.models.auth.User;
import com.j23.server.repos.auth.AuthRepository;
import com.j23.server.services.encryptDecrypt.EncryptDecryptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthRepository authRepository;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public List<User> findAllUser() {
        return authRepository.findAll();
    }

    public User updateUser(User user) {
//    if (clientRepo.existsByUsernameAndIdIsNot(client.getUsername(), client.getId())) {
//      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
//    }
        return authRepository.save(user);
    }

    public void resetUserPassword(String username) {
        User user = authRepository.findUserByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User does not exists"));
        user.setUserPassword(getEncodedPassword("1234"));
        authRepository.save(user);
    }

    public void changeUserPassword(ChangePassword changePassword) {
        User user = authRepository.findUserByUsername(changePassword.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User does not exists"));

        // check if password match with old password
//    final UserDetails userDetails = jwtService.loadUserByUsername(changePassword.getUsername());
//    jwtService.authenticate(client.getUsername(), changePassword.getOldPassword());

        user.setUserPassword(getEncodedPassword(changePassword.getNewPassword()));
        authRepository.save(user);
    }

    public User findUserByDecryptedUsername(String username) throws Exception {
        String decryptedUsername = EncryptDecryptService.decrypt(username);
        return authRepository.findUserByUsername(decryptedUsername).orElseThrow(() ->
                new UserNotFoundException("username " + username + " was not found"));
    }

    public void deleteUserByUsername(String username) {
        authRepository.deleteByUsername(username);
    }

    public User registerNewUser(User user) {
        if (authRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        user.setUserPassword(getEncodedPassword("1234"));
        return authRepository.save(user);
    }

//  public void initRolesAndUser() {
//    Role adminRole = new Role();
//    adminRole.setRoleName("Admin");
//    adminRole.setRoleDescription("Admin role");
//    roleRepo.save(adminRole);
//
//    Role userRole = new Role();
//    userRole.setRoleName("User");
//    userRole.setRoleDescription("User role");
//    roleRepo.save(userRole);
//
//    Role customerRole = new Role();
//    customerRole.setRoleName("Customer");
//    customerRole.setRoleDescription("Customer role");
//    roleRepo.save(customerRole);
////
//    Role cashierRole = new Role();
//    cashierRole.setRoleName("Cashier");
//    cashierRole.setRoleDescription("Cashier role");
//    roleRepo.save(cashierRole);
//
//
////        create user with role
//    Client adminClient = new Client();
//    adminClient.setId("Iam Cool");
//    adminClient.setUserFirstName("Admin");
//    adminClient.setUserLastName("Admin");
//    adminClient.setGender("Male");
//    adminClient.setAddress("Purimas Regency B6 no 2");
//    adminClient.setBankAccount("1291892812");
//    adminClient.setEmail("jeremywib7@gmail.com");
//    adminClient.setDateJoined(LocalDate.now());
//    adminClient.setActive(true);
//    adminClient.setImageUrl("profile_picture.png");
//    adminClient.setUsername("Admin");
//    adminClient.setUserPassword(getEncodedPassword("admin@pass"));
//    adminClient.setPhoneNumber("081226974475");
//    adminClient.setRole(adminRole);
//    clientRepo.save(adminClient);
//
//    Client customerClient = new Client();
//    customerClient.setId("1234aa");
//    customerClient.setUserFirstName("John");
//    customerClient.setUserLastName("Doe");
//    adminClient.setGender("female");
//    adminClient.setEmail("mastah@gmail.com");
//    customerClient.setDateJoined(LocalDate.now());
//    customerClient.setActive(true);
//    customerClient.setImageUrl("profile_picture.png");
//    customerClient.setUsername("John");
//    customerClient.setUserPassword(getEncodedPassword("customer@pass"));
//    customerClient.setPhoneNumber("07182671267");
//    customerClient.setRole(customerRole);
//    clientRepo.save(customerClient);
//
//  }

    public void deleteSelectedUsers(List<String> username) {
        username.forEach(userId -> {
            try {
                authRepository.deleteByUsername(userId);
            } catch (Exception ignored) {
            }
        });
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder().encode(password);
    }
}
