package com.j23.server.controllers.auth;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.auth.JwtRequest;
import com.j23.server.models.auth.JwtResponse;
import com.j23.server.services.auth.JwtService;
import com.j23.server.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class JwtController {
    //
    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) {
//        test cookie
//        Cookie cookie = new Cookie("cookie", "one_cookie_for_you");

//        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
//        cookie.setSecure(false);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        cookie.setDomain("localhost");

//        response.addCookie(cookie);
        return jwtService.createJwtToken(jwtRequest);
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
