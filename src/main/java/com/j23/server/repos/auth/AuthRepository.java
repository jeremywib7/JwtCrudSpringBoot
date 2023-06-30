package com.j23.server.repos.auth;

import com.j23.server.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    void deleteByUsername(String username);
    boolean existsByUsernameAndIdIsNot(String username, String id);

    User findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
