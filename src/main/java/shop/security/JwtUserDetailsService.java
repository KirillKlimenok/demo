package shop.security;

import shop.model.User;
import shop.security.jwt.JwtUser;
import shop.security.jwt.JwtUserFactory;
import shop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final JwtUserFactory jwtUserFactory;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Mono<User> userMono = userService.findByLogin(login)
                .filter(Objects::nonNull);

        AtomicReference<User> user = new AtomicReference<>(new User());

        userMono.subscribe(user::set);

        return createUserDetails(login, user.get());
    }

    private UserDetails createUserDetails(String login, User user) {
        JwtUser jwtUser = jwtUserFactory.create(user);
        log.info("In JwtUserDetailsService - loadUserByUsername() user with username : {} loaded", login);
        return jwtUser;
    }
}
