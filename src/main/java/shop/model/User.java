package shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Generated;


@Data
@Builder
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

    public boolean isNew(){
        return true;
    }
}
