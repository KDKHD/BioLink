package com.BioLink.UserSecurity.security.oauth2.refreshToken;

import lombok.Data;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.time.Instant;

@Data
public class InstagramOauth2RefreshTokenResponse {
    private String access_token;
    private String token_type;
    private Integer expires_in;

    public OAuth2RefreshToken refreshToken() {
        return new OAuth2RefreshToken(access_token, null, Instant.now().plusSeconds(expires_in));
    }
}
