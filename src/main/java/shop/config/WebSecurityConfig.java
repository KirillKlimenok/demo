package shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain  securityWebFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/auth", "/reg").permitAll()
                .pathMatchers("/admin").hasRole("ROLE_ADMIN")
                .anyExchange().authenticated()
                .and()
                .build();
    }
}
