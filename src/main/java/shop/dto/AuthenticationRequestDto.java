package shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import shop.model.Role;

@Data
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String login;
    private String password;
    private Role role;
}
