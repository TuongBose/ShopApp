package com.project.Shopapp.Configurations;

import com.project.Shopapp.Filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    "/api/v1/accounts/register",
                                    "/api/v1/accounts/login"
                            ).permitAll()
                            .requestMatchers(PUT, "api/v1/donhangs/**").hasRole("ADMIN")
                            .requestMatchers(POST, "api/v1/donhangs/**").hasRole("USER")
                            .requestMatchers(DELETE, "api/v1/donhangs/**").hasRole("ADMIN")
                            .requestMatchers(GET, "api/v1/donhangs/**").hasAnyRole("USER", "ADMIN")
                            .anyRequest().authenticated();
                })
                .build();
    }
}
