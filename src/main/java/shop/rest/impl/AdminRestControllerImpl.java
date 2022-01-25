package shop.rest.impl;

import shop.exception.UserNotFoundException;
import shop.model.User;
import shop.rest.AdminRestController;
import shop.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminRestControllerImpl implements AdminRestController {
    private final UserService userService;

    @Override
    public Mono<User> getUserById(@NonNull String id) throws UserNotFoundException {
        try {
            return userService.findById(UUID.fromString(id));
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("user with with " + id + " now found");
        }
    }

}
