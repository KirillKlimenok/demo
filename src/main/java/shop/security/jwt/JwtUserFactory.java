package shop.security.jwt;

import shop.model.Role;
import shop.model.Status;
import shop.model.User;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class JwtUserFactory {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public JwtUser create(User user) {
        return JwtUser.
                builder().
                id(user.getId()).
                login(user.getLogin()).
                email(user.getEmail()).
                password(user.getPassword()).
                isEnabled(user.getStatus().equals(Status.ACTIVE)).
                lastPasswordResetDate(Date.from(user.getUpdated().toInstant(ZoneOffset.UTC))).
//                authorities(mapToGrantedAuthorities(user.getRoles())).
        build();
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.
                stream().
                map(role -> new SimpleGrantedAuthority(role.getName())).
                collect(Collectors.toList());
    }
}
