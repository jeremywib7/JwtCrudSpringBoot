package com.j23.server.repos.auth;

import com.j23.server.models.auth.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<Role, String> {

}
