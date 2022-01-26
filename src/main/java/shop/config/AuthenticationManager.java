package shop.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        String username;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            username = null;
            log.info(e.toString());
        }

        if (username != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getClaimsFromToken(token);
            List<String> role = claims.get("role", List.class);

            List<SimpleGrantedAuthority> authorities = role.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );
            return Mono.just(authenticationToken);
        } else {
            return Mono.empty();
        }
    }
}
