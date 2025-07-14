package com.project.Shopapp.configurations;

import com.project.Shopapp.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebMvc
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("%s/accounts/register",apiPrefix),
                                    String.format("%s/accounts/login",apiPrefix)
                            ).permitAll()
                            //.requestMatchers("**").permitAll()
                            .requestMatchers(GET, String.format("%s/loaisanphams**",apiPrefix)).permitAll()
                            .requestMatchers(POST, String.format("%s/loaisanphams/**",apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/loaisanphams/**",apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/loaisanphams/**",apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(GET, String.format("%s/sanphams/**",apiPrefix)).permitAll()
                            .requestMatchers(GET, String.format("%s/sanphams/images/**",apiPrefix)).permitAll()
                            .requestMatchers(POST, String.format("%s/sanphams/**",apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(PUT, String.format("%s/sanphams/**",apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/sanphams/**",apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(PUT, String.format("%s/donhangs/**",apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/donhangs/**",apiPrefix)).hasRole("USER")
                            .requestMatchers(DELETE, String.format("%s/donhangs/**",apiPrefix)).hasRole("ADMIN")
                            //.requestMatchers(GET, String.format("%s/donhangs/get-alldonhang-by-keyword").hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/donhangs/**",apiPrefix)).permitAll()
                            .requestMatchers(PUT, String.format("%s/donhangs/status",apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(PUT, String.format("%s/chitietdonhangs/**",apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/chitietdonhangs/**",apiPrefix)).hasRole("USER")
                            .requestMatchers(DELETE, String.format("%s/chitietdonhangs/**",apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/chitietdonhangs/**",apiPrefix)).hasAnyRole("USER", "ADMIN")

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
