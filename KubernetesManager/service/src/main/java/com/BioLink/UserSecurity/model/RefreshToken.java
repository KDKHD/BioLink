package com.BioLink.UserSecurity.model;

import lombok.*;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Data
@Entity
@Table(name = "refreshTokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private String tokenValue;

    @Column(nullable = true)
    private Instant issuedAt;

    @Column(nullable = true)
    private Instant expiresAt;

    public RefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        this.tokenValue = oAuth2RefreshToken.getTokenValue();
        this.issuedAt = oAuth2RefreshToken.getIssuedAt();
        this.expiresAt = oAuth2RefreshToken.getExpiresAt();
    }
}
