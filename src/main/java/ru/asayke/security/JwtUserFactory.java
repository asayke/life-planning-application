package ru.asayke.security;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.enums.Role;
import ru.asayke.entity.enums.Status;

import java.util.List;

@NoArgsConstructor
public final class JwtUserFactory {
    public static JwtUser create(ApplicationUser applicationUser) {
        return new JwtUser(
                applicationUser.getId(),
                applicationUser.getEmail(),
                applicationUser.getUsername(),
                applicationUser.getPassword(),
                mapRolesToGrantedAuthorities(applicationUser.getRole()),
                applicationUser.getStatus().equals(Status.ACTIVE)
        );
    }

    private static List<GrantedAuthority> mapRolesToGrantedAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
