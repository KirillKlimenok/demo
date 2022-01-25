package shop.rest;

import shop.exception.UserNotFoundException;
import shop.model.User;
import lombok.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
public interface AdminRestController {
    @GetMapping("users/{id}")
    Mono<User> getUserById(@PathVariable(name = "id")
                           @NonNull String id) throws UserNotFoundException;
}
