package shop.config;

import shop.security.jwt.JwtConfig;
import shop.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "admin/**";
    private static final String LOGIN_ENDPOINT = "login";
    private static final String REGISTRATION_ENDPOINT = "reg";

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                httpBasic().
                disable().
                csrf().
                disable().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers(LOGIN_ENDPOINT).
                permitAll().
                antMatchers(REGISTRATION_ENDPOINT).
                permitAll().
                antMatchers(ADMIN_ENDPOINT).
                hasRole("ADMIN").
                anyRequest().
                permitAll().
                and().
                apply(new JwtConfig(jwtTokenProvider));
    }
}
