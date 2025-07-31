package com.project.Shopapp.configurations;

import com.project.Shopapp.models.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private final WebClient userInfoClient;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        Account userInfo = userInfoClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(Account.class)
                .block();
        Map<String, Object> attributes = new HashMap<>();
        assert userInfo != null;
        attributes.put("userId", userInfo.getUSERID());
        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.getUsername(), attributes, null);
    }
}
