package shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user_role")
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseEntity{

    @Column(value = "id_user")
    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
    private UUID userId;

    @Column(value = "id_role")
    private UUID roleId;

    @Builder
    public UserRole(UUID id, LocalDateTime created, LocalDateTime updated, Status status, UUID userId, UUID roleId) {
        super(id, created, updated, status);
        this.userId = userId;
        this.roleId = roleId;
    }
}
