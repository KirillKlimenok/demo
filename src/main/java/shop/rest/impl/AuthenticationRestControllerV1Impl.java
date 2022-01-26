package shop.rest.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import shop.config.JwtUtil;
import shop.dto.AuthenticationResponseDto;
import shop.model.UserAndRole;
import shop.rest.AuthenticationRestControllerV1;
import shop.service.UserService;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthenticationRestControllerV1Impl implements AuthenticationRestControllerV1 {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<ResponseEntity> login(@RequestBody ServerWebExchange serverWebExchange) {
        try {
            return serverWebExchange.getFormData().flatMap(credentials -> userService
                    .findByLogin(credentials.getFirst("username"))
                    .cast(UserAndRole.class)
                    .map(userDetails -> {
                        if (Objects.equals(
                                credentials.getFirst("password"),
                                userDetails.getUser().getPassword())) {
                            return ResponseEntity.ok(AuthenticationResponseDto.builder()
                                    .login(userDetails.getUsername())
                                    .token(jwtUtil.generateToken(userDetails))
                                    .build());
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                        }
                    })
            );

        } catch (AuthenticationException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }
}
