package com.j23.server.services.auth;

import com.j23.server.exception.UserNotFoundException;
import com.j23.server.models.auth.Role;
import com.j23.server.models.auth.User;
import com.j23.server.repos.auth.RoleRepo;
import com.j23.server.repos.auth.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return userRepo.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepo.findById(username).orElseThrow(() -> new UserNotFoundException("username " + username + " was not found"));
    }

    public void deleteUserByUsername(String username) {
        userRepo.deleteById(username);
    }

    public User registerNewUser(User user) {
        user.setUserCode(String.valueOf(UUID.randomUUID()));
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

//        create user with role
        User adminUser = new User();
        adminUser.setUserFirstName("Admin");
        adminUser.setUserLastName("Admin");
        adminUser.setDateJoined(LocalDate.now());
        adminUser.setActive(true);
        adminUser.setUsername("Admin");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setRole(adminRole);
        userRepo.save(adminUser);

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
