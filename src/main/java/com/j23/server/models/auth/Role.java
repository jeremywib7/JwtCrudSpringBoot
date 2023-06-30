package com.j23.server.models.auth;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class Role {

    @Id
    @NonNull
    private String name;

//    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
//    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

//    public List<SimpleGrantedAuthority> getAuthorities() {
//        var authorities = getPermissions()
//                .stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
//                .collect(Collectors.toList());
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + getName()));
//        return authorities;
//    }
//
//    USER(Collections.emptySet()),
//    ADMIN(
//            Set.of(
//                    ADMIN_READ,
//                    ADMIN_UPDATE,
//                    ADMIN_DELETE,
//                    ADMIN_CREATE,
//                    MANAGER_READ,
//                    MANAGER_UPDATE,
//                    MANAGER_DELETE,
//                    MANAGER_CREATE
//            )
//    ),
//    MANAGER(
//            Set.of(
//                    MANAGER_READ,
//                    MANAGER_UPDATE,
//                    MANAGER_DELETE,
//                    MANAGER_CREATE
//            )
//    );


}
