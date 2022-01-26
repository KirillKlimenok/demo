package shop.service.impl;

import shop.dto.RegistrationResponseDto;
import shop.dto.RoleResponseDto;
import shop.exception.UserNotFoundException;
import shop.model.Role;
import shop.model.Status;
import shop.model.User;
import shop.model.UserRole;
import shop.repository.RoleRepository;
import shop.repository.UserRepository;
import shop.repository.UserRoleRepository;
import shop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Mono<RegistrationResponseDto> save(User user) {
        return roleRepository.
                findAll().
                filter(x -> x.getName().equals("ROLE_USER")).
                switchIfEmpty(Mono.error(new NullPointerException("role list empty"))).
                next().map(role1 -> {
                    RoleResponseDto roleResponseDto = new RoleResponseDto(role1.getName());
                    User currentUser = configureUser(user).build();

                    userRepository.
                            save(currentUser).
                            block();

                    User userInDb = userRepository.
                            findByLogin(currentUser.getLogin()).
                            switchIfEmpty(Mono.error(new UserNotFoundException("user nowt found: " + currentUser.getLogin()))).
                            block();

                    UserRole userRole = setUserRoleValues(role1, currentUser.getCreated(), userInDb.getId())
                            .build();

                    userRoleRepository.save(userRole).subscribe();

                    return new RegistrationResponseDto(user.getLogin(), List.of(roleResponseDto));
                }).doOnNext(x -> {
                    log.info("user: {} created", x.getLogin());
                    log.info("userRole: {} created", x.getRoleList());
                });
    }

    private User.UserBuilder configureUser(User user) {
        LocalDateTime date = LocalDateTime.now();

        return User
                .builder()
                .id(null)
                .login(user.getLogin())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .created(date)
                .updated(date)
                .status(Status.ACTIVE);
    }

    private UserRole.UserRoleBuilder setUserRoleValues(Role role, LocalDateTime date, UUID userId) {
        return UserRole
                .builder()
                .roleId(role.getId())
                .userId(userId)
                .created(date)
                .updated(date)
                .status(Status.ACTIVE);
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return userRepository.deleteById(id).then();
    }
}
