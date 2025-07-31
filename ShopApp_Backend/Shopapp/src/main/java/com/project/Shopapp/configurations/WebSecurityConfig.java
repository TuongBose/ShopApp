package com.project.Shopapp.configurations;

import com.project.Shopapp.filters.JwtTokenFilter;
import com.project.Shopapp.models.Role;
import jakarta.websocket.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
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
                //.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(customizer->customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("%s/accounts/register", apiPrefix),
                                    String.format("%s/accounts/login", apiPrefix),
                                    String.format("%s/accounts/refreshToken", apiPrefix),
                                    String.format("%s/healthcheck/**", apiPrefix),
                                    String.format("%s/actuator/**", apiPrefix),

                                    "/api-docs",
                                    "/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/webjars/swagger-ui/**",
                                    "/swagger-ui/index.html"
                            
                            ).permitAll()
                            //.requestMatchers("**").permitAll()

                            // feedbacks
                            .requestMatchers(GET,String.format("%s/feedbacks/**",apiPrefix)).permitAll()

                            // coupons
                            .requestMatchers(GET,String.format("%s/coupons/**",apiPrefix)).permitAll()

                            // loaisanphams
                            .requestMatchers(GET, String.format("%s/loaisanphams/**", apiPrefix)).permitAll()
                            .requestMatchers(POST, String.format("%s/loaisanphams/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/loaisanphams/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE, String.format("%s/loaisanphams/**", apiPrefix)).hasRole(Role.ADMIN)

                            // sanphams
                            .requestMatchers(GET, String.format("%s/sanphams/**", apiPrefix)).permitAll()
                            .requestMatchers(GET, String.format("%s/sanphams/images/**", apiPrefix)).permitAll()
                            .requestMatchers(POST, String.format("%s/sanphams/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/sanphams/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE, String.format("%s/sanphams/**", apiPrefix)).hasRole(Role.ADMIN)

                            // donhangs
                            .requestMatchers(PUT, String.format("%s/donhangs/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(POST, String.format("%s/donhangs/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(DELETE, String.format("%s/donhangs/**", apiPrefix)).hasRole(Role.ADMIN)

                            /*
                             If you comment this, you must add @PreAuthorize("hasRole('ROLE_ADMIN')") at controller
                            .requestMatchers(GET, String.format("%s/donhangs/get-alldonhang-by-keyword",apiPrefix)).hasRole(Role.ADMIN)
                            */

                            .requestMatchers(GET, String.format("%s/donhangs/**", apiPrefix)).permitAll()
                            .requestMatchers(PUT, String.format("%s/donhangs/status", apiPrefix)).hasRole(Role.ADMIN)

                            // chitietdonhangs
                            .requestMatchers(PUT, String.format("%s/chitietdonhangs/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST, String.format("%s/chitietdonhangs/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(DELETE, String.format("%s/chitietdonhangs/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/chitietdonhangs/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)

                            .anyRequest().authenticated();
                })

                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(c->c.opaqueToken(Customizer.withDefaults()))
                .cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of("*"));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                        configuration.setExposedHeaders(List.of("x-auth-token"));
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**", configuration);
                        httpSecurityCorsConfigurer.configurationSource(source);
                    }
                })
                .securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()))
                .build();
    }
}
