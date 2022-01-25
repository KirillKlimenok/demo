package shop.rest.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.dto.RegistrationRequestDto;
import shop.dto.RegistrationResponseDto;
import shop.mapper.UserMapper;
import shop.rest.RegistrationRestController;
import shop.service.UserService;

@Component
@RequiredArgsConstructor
public class RegistrationRestControllerImpl implements RegistrationRestController {
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public RegistrationResponseDto registration(RegistrationRequestDto registrationRequestDto) {

        return userService.save(userMapper.toModel(registrationRequestDto));
    }
}
