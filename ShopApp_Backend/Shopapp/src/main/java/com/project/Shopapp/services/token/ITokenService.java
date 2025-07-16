package com.project.Shopapp.services.token;

import com.project.Shopapp.models.Account;

public interface ITokenService {
    void addToken(Account account, String token, boolean isMobileDevice);
}
