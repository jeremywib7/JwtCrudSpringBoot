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
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class JwtController {
//
    @Autowired
    private JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest, HttpServletResponse response) throws Exception {
//        test cookie
        Cookie cookie = new Cookie("cookie", "one_cookie_for_you");

        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        cookie.setDomain("localhost");

        response.addCookie(cookie);

        return jwtService.createJwtToken(jwtRequest);
    }

}
