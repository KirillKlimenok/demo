package shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import shop.dto.RoleResponseDto;

@Data
@AllArgsConstructor
public class UserAndRole {
    private User user;
    private Role role;
}
