package com.j23.server.controllers.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.auth.User;
import com.j23.server.repos.auth.UserRepo;
import com.j23.server.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    JsonNode json;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesAndUser();
    }

    @PostMapping({"/register"})
    public ResponseEntity<Object> registerNewUser(@RequestBody User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Username already exists");
        }

        User result = userService.registerNewUser(user);

        return ResponseHandler.generateResponse("Successfully registered user!", HttpStatus.OK, result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = (List<User>) userService.findAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return ResponseHandler.generateResponse("Successfully update user!", HttpStatus.OK, updateUser);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {

        try {
            userService.deleteUserByUsername(username);
            json = mapper.readTree("{\"status\": 200, \"message\": \"User " + username + " deleted successfully\"}");

            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is not found");
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid JSON object");
        }

    }

    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin() {
        return "This URL is only accessible to admin";
    }

    @GetMapping("/forUser")
    @PreAuthorize("hasRole('User')")
    public String forUser() {
        return "This URL is only accessible to user";
    }


}
