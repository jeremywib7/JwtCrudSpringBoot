package com.j23.server.controllers.auth;

import com.j23.server.models.auth.JwtRequest;
import com.j23.server.models.auth.JwtResponse;
import com.j23.server.services.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class JwtController {
//
    @Autowired
    private JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest, HttpServletResponse res) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

}
