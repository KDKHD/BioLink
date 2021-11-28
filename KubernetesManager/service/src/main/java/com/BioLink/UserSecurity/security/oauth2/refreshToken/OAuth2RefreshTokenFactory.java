package com.BioLink.UserSecurity.security.oauth2.refreshToken;

import com.BioLink.UserSecurity.exception.OAuth2AuthenticationProcessingException;
import com.BioLink.UserSecurity.model.AuthProvider;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

public class OAuth2RefreshTokenFactory {
    public static OAuth2RefreshTokenExchange getOAuth2RefreshToken(String registrationId, OAuth2AccessToken accessToken) {
        if(registrationId.equalsIgnoreCase(AuthProvider.instagram.toString())) {
            return new InstagramOauth2RefreshToken(accessToken);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! RefreshTokens for " + registrationId + " are not supported yet.");
        }
    }
}
