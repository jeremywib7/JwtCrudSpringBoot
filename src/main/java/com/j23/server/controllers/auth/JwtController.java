package com.j23.server.controllers.auth;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.auth.*;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.services.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

@RestController
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerProfileRepo customerProfileRepo;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) {
        return jwtService.createJwtTokenForUser(jwtRequest);
    }

    @GetMapping("/checkJWT")
    public ResponseEntity<Object> checkJWTExpired(@RequestParam String jwtToken) {

        Map<String, Object> map = new LinkedHashMap<>();

        DecodedJWT jwt = JWT.decode(jwtToken);
        if (jwt.getExpiresAt().before(new Date())) {
            map.put("status", "expired");
        } else {
            map.put("status", "ok");
        }

        return ResponseHandler.generateResponse("Successfully check JWT Expiry!", HttpStatus.OK, map);
    }
}
