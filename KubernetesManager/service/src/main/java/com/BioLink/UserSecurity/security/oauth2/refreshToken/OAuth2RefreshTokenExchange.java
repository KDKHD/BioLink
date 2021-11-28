package com.BioLink.UserSecurity.security.oauth2.refreshToken;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.util.concurrent.ExecutionException;

public abstract class OAuth2RefreshTokenExchange {
    protected OAuth2AccessToken accessToken;
    protected OAuth2RefreshToken refreshToken;

    public OAuth2RefreshTokenExchange(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public OAuth2RefreshTokenExchange(OAuth2RefreshToken accessToken) {
        this.refreshToken = refreshToken;
    }


    public abstract OAuth2AccessToken getAccessToken();

    public abstract OAuth2RefreshTokenExchange exchangeAccessTokenForRefreshToken() throws ExecutionException, InterruptedException;

    public abstract OAuth2RefreshTokenExchange exchangeRefreshTokenForRefreshToken() throws ExecutionException, InterruptedException;

    public abstract OAuth2RefreshToken getRefreshToken();

}
