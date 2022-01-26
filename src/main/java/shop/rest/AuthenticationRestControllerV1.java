package shop.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/")
public interface AuthenticationRestControllerV1 {
    @PostMapping("/login")
    Mono<ResponseEntity> login(@RequestBody ServerWebExchange requestDto);
}
