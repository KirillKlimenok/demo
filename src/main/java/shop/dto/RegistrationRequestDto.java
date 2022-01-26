package shop.dto;

import liquibase.pro.packaged.S;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {
    private String login;
    private String password;
}
