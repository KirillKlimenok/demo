package shop.service.impl;

import shop.dto.RegistrationResponseDto;
import shop.dto.RoleResponseDto;
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
    public RegistrationResponseDto save(User user) {
        Role role = roleRepository.
                findAll().
                filter(x -> x.getName().equals("ROLE_USER")).
                next().
                block();

        UserRole userRole = new UserRole();
        User currentUser = configureUser(role, user, userRole);
        RoleResponseDto roleResponseDto = new RoleResponseDto(role.getName());

        userRepository.save(currentUser).block();

        User userInDb = userRepository.findByLogin(currentUser.getLogin()).block();

        userRole.setUserId(userInDb.getId());
        userRoleRepository.save(userRole).subscribe();

        log.info("user: {} created", currentUser);
        log.info("userRole: {} created", userRole);

        return new RegistrationResponseDto(user.getLogin(), List.of(roleResponseDto));
    }

    private User configureUser(Role role, User user, UserRole userRole) {
        LocalDateTime date = LocalDateTime.now();
        UUID userId = UUID.randomUUID();

        setUserRoleValues(userRole, role, date, userId);

        user.setId(null);
        user.setStatus(Status.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(date);
        user.setUpdated(date);

        return user;
    }

    private void setUserRoleValues(UserRole userRole, Role role, LocalDateTime date, UUID userId) {
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        userRole.setCreated(date);
        userRole.setUpdated(date);
        userRole.setStatus(Status.ACTIVE);
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
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
