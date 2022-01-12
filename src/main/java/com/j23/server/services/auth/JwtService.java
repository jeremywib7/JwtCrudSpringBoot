package com.j23.server.services.auth;

import com.j23.server.models.auth.JwtRequest;
import com.j23.server.models.auth.JwtResponse;
import com.j23.server.models.auth.User;
import com.j23.server.repos.auth.UserRepo;
import com.j23.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) {
        String userName = jwtRequest.getUserName();
        String userPass = jwtRequest.getUserPassword();

        authenticate(userName, userPass);

        final UserDetails userDetails = loadUserByUsername(userName);

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userRepo.findById(userName).get();

        return new JwtResponse(user, newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getUserPassword(),
                    getAuthorities(user));
        } else {
            throw new UsernameNotFoundException("Username is not valid");
        }
    }

    private Set getAuthorities(User user) {
        Set authorities = new HashSet();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
//        user.getRole().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
//        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User is disabled");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Wrong username or password");
        }
    }
}
