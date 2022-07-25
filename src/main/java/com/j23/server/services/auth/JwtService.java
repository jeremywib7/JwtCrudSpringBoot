package com.j23.server.services.auth;

import com.j23.server.models.auth.*;
import com.j23.server.repos.customer.CustomerProfileRepo;
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
  private CustomerProfileRepo customerProfileRepo;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private AuthenticationManager authenticationManager;

  //     for internal
  public JwtResponse createJwtTokenForUser(JwtRequest jwtRequest) {
    String userName = jwtRequest.getUserName();
    String userPass = jwtRequest.getUserPassword();

    final UserDetails userDetails = loadUserByUsername(userName);

    authenticate(userName, userPass);

    String newGeneratedToken = jwtUtil.generateJwtToken(userDetails);
    String refreshToken = jwtUtil.generateRefreshToken(userDetails);

    User user = userRepo.findByUsername(userName);

    return new JwtResponse(user, newGeneratedToken, refreshToken);
  }

  public UserDetails loadUserById(String id) {
    User user = userRepo.findById(id).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exists"));

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getUserPassword(),
      getAuthorities(user));
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepo.findUserByUsername(username).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username does not exists"));

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getUserPassword(),
      getAuthorities(user));
  }

  private Set getAuthorities(User user) {
    Set authorities = new HashSet();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));

    return authorities;
  }

  public void authenticate(String userName, String userPassword) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
    } catch (DisabledException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is disabled");
    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password is incorrect");
    }
  }
}
