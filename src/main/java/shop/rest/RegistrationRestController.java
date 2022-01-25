package shop.rest;

import shop.dto.RegistrationRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.RegistrationResponseDto;

import java.util.List;

@RestController
@RequestMapping("/reg")
public interface RegistrationRestController {
    @PostMapping
    RegistrationResponseDto registration(@RequestBody RegistrationRequestDto registrationRequestDto);
}
