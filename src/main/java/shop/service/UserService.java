package shop.service;

import shop.dto.RegistrationResponseDto;
import shop.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface UserService {
    RegistrationResponseDto save(User user);

    Flux<User> findAll();

    Mono<User> findByLogin(String login);

    Mono<User> findById(UUID id);

    void delete(UUID id);
}
