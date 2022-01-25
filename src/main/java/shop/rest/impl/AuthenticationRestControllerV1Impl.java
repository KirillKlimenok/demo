package shop.rest.impl;

import shop.dto.AuthenticationRequestDto;
import shop.dto.AuthenticationResponseDto;
import shop.model.Role;
import shop.model.User;
import shop.rest.AuthenticationRestControllerV1;
import shop.security.jwt.JwtTokenProvider;
import shop.service.UserService;
import liquibase.pro.packaged.A;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthenticationRestControllerV1Impl implements AuthenticationRestControllerV1 {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public AuthenticationResponseDto login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String login = requestDto.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, requestDto.getPassword()));
            Mono<User> user = userService.findByLogin(login);

            if (user == null) {
                throw new BadCredentialsException("Invalid username or password");
            }

//            Flux<Role> roleFlux = user
//                    .filter(Objects::nonNull)
//                    .flatMapMany(x -> Flux.fromArray(x.toArray(new Role[2])));

//            String token = jwtTokenProvider.createToken(login, roleFlux);

            return new AuthenticationResponseDto(login, "token");
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
