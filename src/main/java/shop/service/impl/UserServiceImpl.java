package shop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shop.dto.RegistrationResponseDto;
import shop.dto.RoleResponseDto;
import shop.exception.UserNotFoundException;
import shop.model.Status;
import shop.model.User;
import shop.model.UserAndRole;
import shop.model.UserRole;
import shop.repository.RoleRepository;
import shop.repository.UserRepository;
import shop.repository.UserRoleRepository;
import shop.service.UserService;

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
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<RegistrationResponseDto> save(User user) {
        return roleRepository.
                findAll().
                filter(x -> x.getName().equals("ROLE_USER")).
                switchIfEmpty(Mono.error(new NullPointerException("role list empty"))).
                next().
                map(role -> {
                    LocalDateTime date = LocalDateTime.now();

                    User currentUser = User
                            .builder()
                            .id(null)
                            .login(user.getLogin())
                            .email(user.getEmail())
                            .password(passwordEncoder.encode(user.getPassword()))
                            .created(date)
                            .updated(date)
                            .status(Status.ACTIVE)
                            .build();

                    userRepository
                            .save(currentUser).subscribe();

                    return new UserAndRole(currentUser, role);
                }).
                flatMap(userAndRole -> userRepository.
                        findByLogin(userAndRole.getUser().getLogin()).
                        switchIfEmpty(Mono.
                                error(new UserNotFoundException("user nowt found: " + userAndRole.getUser().getLogin()))).
                        map(userInBd -> {
                            userAndRole.setUser(userInBd);
                            return userAndRole;
                        })).
                map(userAndRole -> {
                    userRoleRepository.save(
                            UserRole
                                    .builder()
                                    .roleId(userAndRole.getRole().getId())
                                    .userId(userAndRole.getUser().getId())
                                    .created(userAndRole.getUser().getCreated())
                                    .updated(userAndRole.getUser().getUpdated())
                                    .status(Status.ACTIVE)
                                    .build()
                    );

                    return RegistrationResponseDto
                            .builder()
                            .login(user.getLogin())
                            .roleList(List.of(RoleResponseDto
                                    .builder()
                                    .name(userAndRole
                                            .getRole()
                                            .getName())
                                    .build()))
                            .build();
                }).
                doOnNext(x -> {
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
