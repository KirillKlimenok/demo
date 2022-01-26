package shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthenticationResponseDto {
   private String login;
   private String token;
}
