package com.project.Shopapp.configurations;

import com.project.Shopapp.models.Account;
import com.project.Shopapp.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AccountRepository accountRepository;

    // Account detail object
    @Bean
    public UserDetailsService accountDetailsService() {
        return subject -> {
            // Attempt to find account by phone number
            Optional<Account> accountByPhoneNumber = accountRepository.findBySODIENTHOAI(subject);
            if (accountByPhoneNumber.isPresent()) {
                return accountByPhoneNumber.get(); // Return UserDetails if found
            }
            // If user not found by phone number, attempt to find account by email
            Optional<Account> accountByEmail = accountRepository.findByEMAIL(subject);
            if (accountByEmail.isPresent()) {
                return accountByEmail.get(); // Return UserDetails if found
            }

            // If user not found by either phone number or email, throw UsernameNotFoundException
            throw new UsernameNotFoundException("User not found with subject: " + subject);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
