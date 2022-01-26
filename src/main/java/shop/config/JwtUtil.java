package shop.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.dto.AuthenticationRequestDto;
import shop.dto.RegistrationRequestDto;
import shop.model.UserAndRole;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtUtil {
    private String secret = "secret";

    private String expirationTime = "86400";

    public boolean validateToken(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    public String extractUsername(String token) {
        return getClaimsFromToken(token)
                .getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        String key = Base64
                .getEncoder()
                .encodeToString(secret.getBytes());

        return Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserAndRole user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", List.of(user.getRole().getName()));

        long expirationSeconds = Long.parseLong(expirationTime);
        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() + expirationSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }
}
