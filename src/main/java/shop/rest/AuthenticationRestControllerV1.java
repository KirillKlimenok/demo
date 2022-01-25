package shop.rest;

import shop.dto.AuthenticationRequestDto;
import shop.dto.AuthenticationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public interface AuthenticationRestControllerV1 {
    @PostMapping("/login")
    AuthenticationResponseDto login(@RequestBody AuthenticationRequestDto requestDto);
}
