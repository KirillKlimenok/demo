package shop.repository;

import shop.model.User;
import shop.model.UserRole;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends R2dbcRepository<UserRole, UUID> {
    Mono<UserRole> findByUserId(UUID userId);
    Flux<UserRole> findByRoleId(UUID roleId);
}
