package com.j23.server.models.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Permission {

    @Id
    @Column(unique = true, nullable = false)
    @NonNull
    private String name;

    private String value;

//    @ManyToMany(mappedBy = "permissions")
//    private Set<Role> roles = new HashSet<>();

//    ADMIN_READ("admin:read"),
//    ADMIN_UPDATE("admin:update"),
//    ADMIN_CREATE("admin:create"),
//    ADMIN_DELETE("admin:delete"),
//    MANAGER_READ("management:read"),
//    MANAGER_UPDATE("management:update"),
//    MANAGER_CREATE("management:create"),
//    MANAGER_DELETE("management:delete")
//
//    ;
//
//    @Getter
//    private final String permission;
}
