package com.project.Shopapp.Configurations;

import com.project.Shopapp.Filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
                            //.requestMatchers("**").permitAll()
                            .requestMatchers(GET, "api/v1/loaisanphams**").permitAll()
                            .requestMatchers(POST, "api/v1/loaisanphams/**").hasRole("ADMIN")
                            .requestMatchers(PUT, "api/v1/loaisanphams/**").hasRole("ADMIN")
                            .requestMatchers(DELETE, "api/v1/loaisanphams/**").hasRole("ADMIN")

                            .requestMatchers(GET, "api/v1/sanphams/**").permitAll()
                            .requestMatchers(GET, "api/v1/sanphams/images/**").permitAll()
                            .requestMatchers(POST, "api/v1/sanphams/**").hasRole("ADMIN")
                            .requestMatchers(PUT, "api/v1/sanphams/**").hasRole("ADMIN")
                            .requestMatchers(DELETE, "api/v1/sanphams/**").hasRole("ADMIN")

                            .requestMatchers(PUT, "api/v1/donhangs/**").hasRole("ADMIN")
                            .requestMatchers(POST, "api/v1/donhangs/**").hasRole("USER")
                            .requestMatchers(DELETE, "api/v1/donhangs/**").hasRole("ADMIN")
                            .requestMatchers(GET, "api/v1/donhangs/**").permitAll()

                            .requestMatchers(PUT, "api/v1/chitietdonhangs/**").hasRole("ADMIN")
                            .requestMatchers(POST, "api/v1/chitietdonhangs/**").hasRole("USER")
                            .requestMatchers(DELETE, "api/v1/chitietdonhangs/**").hasRole("ADMIN")
                            .requestMatchers(GET, "api/v1/chitietdonhangs/**").hasAnyRole("USER", "ADMIN")

                            .anyRequest().authenticated();
                })

                .csrf(AbstractHttpConfigurer::disable)

                .cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of("*"));
                        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
                        configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
                        configuration.setExposedHeaders(List.of("x-auth-token"));
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**",configuration);
                        httpSecurityCorsConfigurer.configurationSource(source);
                    }
                })
                .build();
    }
}
