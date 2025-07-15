package com.project.Shopapp.filters;

import com.project.Shopapp.components.JwtTokenUtils;
import com.project.Shopapp.models.Account;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtils.extractPhoneNumber(token);

            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Account userDetails = (Account) userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response); //enable bypass
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isBypassToken(@Nonnull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/sanphams", apiPrefix), "GET"),
                Pair.of(String.format("%s/loaisanphams", apiPrefix), "GET"),
                Pair.of(String.format("%s/accounts/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/accounts/login", apiPrefix), "POST"),

                // Healthcheck, khong yeu cau JWT token
                Pair.of(String.format("%s/healthcheck/health", apiPrefix), "GET"),
                Pair.of(String.format("%s/actuator**", apiPrefix), "GET"),

                // swagger
                Pair.of("/api-docs", "GET"),
                Pair.of("/api-docs/**", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        if (requestPath.startsWith(String.format("/%s/donhangs", apiPrefix))
                && requestMethod.equals("GET")) {
            // Check if the requestPath matches the desired pattern
            if (requestPath.matches(String.format("/%s/donhangs/\\d+", apiPrefix))) {
                return true;
            }
            // If the requestPath is just "api/v1/donhangs", return true
            if (requestPath.equals(String.format("/%s/donhangs", apiPrefix))) {
                return true;
            }
        }

        for (Pair<String, String> bypassToken : bypassTokens) {
            String tokenPath = bypassToken.getFirst();
            String tokenMethod = bypassToken.getSecond();
            // Check if the token  path contains a wildcard character
            if (tokenPath.contains("**")) {
                // Replace "**" with a regular expression capturing any characters
                String regexPath = tokenPath.replace("**", ".*");
                // Create a pattern to match the request path
                Pattern pattern = Pattern.compile(regexPath);
                Matcher matcher = pattern.matcher(requestPath);

                // Check if the request path matches the pattern and the request method matches the token method
                if (matcher.matches() && requestMethod.equals(tokenMethod)) {
                    return true;
                }
            } else if (requestPath.equals(tokenPath) && requestMethod.equals(tokenMethod)) {
                return true;
            }
        }
        return false;
    }
}
