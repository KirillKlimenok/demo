package shop.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import shop.exception.JwtAuthenticationException;
import shop.model.Role;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secret = "secret";
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String userName, Flux<Role> roles) {
        Claims claims = Jwts.
                claims().
                setSubject(userName);
        claims.put("roles", getRoleName(roles));

        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600000);

        return Jwts.
                builder().
                setClaims(claims).
                setIssuedAt(now).
                setExpiration(exp).
                signWith(SignatureAlgorithm.HS256, secret).
                compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getLogin(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "Jwt token", userDetails.getAuthorities());
    }

    public String getLogin(String token) {
        return Jwts.
                parser().
                setSigningKey(secret).
                parseClaimsJws(token).
                getBody().
                getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String baererToken = request.getHeader("Authorization");

        if (baererToken != null && baererToken.startsWith("Bearer_")) {
            return baererToken.substring(7);
        }

        return null;
    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.
                    parser().
                    setSigningKey(secret).
                    parseClaimsJws(token);

            boolean isActual = claimsJws.
                    getBody().
                    getExpiration().
                    before(new Date());

            return !isActual;
        } catch (JwtException | IllegalStateException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    private Flux<String> getRoleName(Flux<Role> userRole) {
        return userRole.
                map(Role::getName);
    }
}
