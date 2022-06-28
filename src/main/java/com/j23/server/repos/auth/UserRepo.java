package com.j23.server.repos.auth;

import com.j23.server.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdIsNot(String username, String id);

    User findByUsername(String username);

    Optional<User> findUserByUsername(String username);
}
