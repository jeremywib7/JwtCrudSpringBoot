package com.j23.server.services.auth;

import com.j23.server.controllers.exception.UserNotFoundException;
import com.j23.server.models.auth.Role;
import com.j23.server.models.auth.User;
import com.j23.server.repos.auth.RoleRepo;
import com.j23.server.repos.auth.UserRepo;
import com.j23.server.services.encryptDecrypt.EncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private RoleRepo roleRepo;

  @Autowired
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public List<User> findAllUser() {
    return userRepo.findAll();
  }

  public User updateUser(User user) {
    if (userRepo.existsByUsernameAndIdIsNot(user.getUsername(), user.getId())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
    }
    return userRepo.save(user);
  }

  public User findUserByUsername(String username) throws Exception {

    String decryptedUsername = EncryptDecryptService.decrypt(username);
    System.out.println("THE USER : " + decryptedUsername);

    return userRepo.findUserByUsername(decryptedUsername).orElseThrow(() ->
      new UserNotFoundException("username " + username + " was not found"));
  }

  public void deleteUserByUsername(String username) {
    userRepo.deleteById(username);
  }

  public User registerNewUser(User user) {
    user.setId(String.valueOf(UUID.randomUUID()));
    user.setUserPassword(getEncodedPassword("1234"));
    return userRepo.save(user);
  }

  public void initRolesAndUser() {
//        create role
    Role adminRole = new Role();
    adminRole.setRoleName("Admin");
    adminRole.setRoleDescription("Admin role");
    roleRepo.save(adminRole);

    Role userRole = new Role();
    userRole.setRoleName("User");
    userRole.setRoleDescription("User role");
    roleRepo.save(userRole);

    Role customerRole = new Role();
    customerRole.setRoleName("Customer");
    customerRole.setRoleDescription("Customer role");
    roleRepo.save(customerRole);
//
    Role cashierRole = new Role();
    cashierRole.setRoleName("Cashier");
    cashierRole.setRoleDescription("Cashier role");
    roleRepo.save(cashierRole);


//        create user with role
    User adminUser = new User();
    adminUser.setId("Iam Cool");
    adminUser.setUserFirstName("Admin");
    adminUser.setUserLastName("Admin");
    adminUser.setGender("Male");
    adminUser.setAddress("Purimas Regency B6 no 2");
    adminUser.setBankAccount("1291892812");
    adminUser.setEmail("jeremywib7@gmail.com");
    adminUser.setDateJoined(LocalDate.now());
    adminUser.setActive(true);
    adminUser.setImageUrl("profile_picture.png");
    adminUser.setUsername("Admin");
    adminUser.setUserPassword(getEncodedPassword("admin@pass"));
    adminUser.setPhoneNumber("081226974475");
    adminUser.setRole(adminRole);
    userRepo.save(adminUser);

    User customerUser = new User();
    customerUser.setId("1234aa");
    customerUser.setUserFirstName("John");
    customerUser.setUserLastName("Doe");
    adminUser.setGender("female");
    adminUser.setEmail("mastah@gmail.com");
    customerUser.setDateJoined(LocalDate.now());
    customerUser.setActive(true);
    customerUser.setImageUrl("profile_picture.png");
    customerUser.setUsername("John");
    customerUser.setUserPassword(getEncodedPassword("customer@pass"));
    customerUser.setPhoneNumber("07182671267");
    customerUser.setRole(customerRole);
    userRepo.save(customerUser);

  }

  public void deleteSelectedUsers(List<String> usersId) {
    usersId.forEach(userId -> {
      try {
        userRepo.deleteById(userId);
      } catch (Exception ignored) {
      }
    });
  }

  public String getEncodedPassword(String password) {
    return passwordEncoder().encode(password);
  }
}
