package com.BioLink.UserSecurity.security.oauth2.refreshToken;


import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ExecutionException;

public class InstagramOauth2RefreshToken extends OAuth2RefreshTokenExchange {

    private WebClient webClient = WebClient.create("https://graph.instagram.com");

    public InstagramOauth2RefreshToken(OAuth2AccessToken accessToken) {
        super(accessToken);
    }

    public InstagramOauth2RefreshToken(OAuth2RefreshToken refreshTokenToken) {
        super(refreshTokenToken);
    }


    @Override
    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return refreshToken;
    }

    @Override
    public OAuth2RefreshTokenExchange exchangeAccessTokenForRefreshToken() throws ExecutionException, InterruptedException {
        refreshToken = webClient
                .get().uri(uriBuilder -> uriBuilder
                        .path("/access_token")
                        .queryParam("grant_type", "ig_exchange_token")
                        .queryParam("client_secret", "1a4b346f269a649d51ba06774e419030")
                        .queryParam("access_token", accessToken.getTokenValue()).build())
                .retrieve()
                .bodyToMono(InstagramOauth2RefreshTokenResponse.class).toFuture().get().refreshToken();
        return this;

    }

    @Override
    public OAuth2RefreshTokenExchange exchangeRefreshTokenForRefreshToken() throws ExecutionException, InterruptedException {
        refreshToken = webClient
                .get().uri(uriBuilder -> uriBuilder
                        .path("/refresh_access_token")
                        .queryParam("grant_type", "ig_refresh_token")
                        .queryParam("client_secret", "1a4b346f269a649d51ba06774e419030")
                        .queryParam("access_token", refreshToken.getTokenValue()).build())
                .retrieve()
                .bodyToMono(InstagramOauth2RefreshTokenResponse.class).toFuture().get().refreshToken();
        return this;
    }

}
