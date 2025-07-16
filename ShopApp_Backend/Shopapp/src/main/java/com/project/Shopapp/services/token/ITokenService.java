package com.project.Shopapp.services.token;

import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Token;

public interface ITokenService {
    Token addToken(Account account, String token, boolean isMobileDevice);
    Token refreshToken (String refreshToken, Account account) throws Exception;
}
