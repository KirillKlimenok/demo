package shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Generated;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "usr")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    @Column(value = "login")
    private String login;

    @Column(value = "email")
    private String email;

    @Column(value = "password")
    private String password;

    @Builder
    public User(UUID id, LocalDateTime created, LocalDateTime updated, Status status, String login, String email, String password) {
        super(id, created, updated, status);
        this.login = login;
        this.email = email;
        this.password = password;
    }
}
