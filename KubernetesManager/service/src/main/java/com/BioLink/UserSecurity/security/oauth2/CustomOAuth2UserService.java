package com.BioLink.UserSecurity.security.oauth2;

import com.BioLink.UserSecurity.exception.OAuth2AuthenticationProcessingException;
import com.BioLink.UserSecurity.model.AuthProvider;
import com.BioLink.UserSecurity.model.RefreshToken;
import com.BioLink.UserSecurity.model.User;
import com.BioLink.UserSecurity.repository.RefreshTokenRepository;
import com.BioLink.UserSecurity.repository.UserRepository;
import com.BioLink.UserSecurity.security.UserPrincipal;
import com.BioLink.UserSecurity.security.oauth2.refreshToken.OAuth2RefreshTokenExchange;
import com.BioLink.UserSecurity.security.oauth2.refreshToken.OAuth2RefreshTokenFactory;
import com.BioLink.UserSecurity.security.oauth2.user.OAuth2UserInfo;
import com.BioLink.UserSecurity.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws ExecutionException, InterruptedException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        try {
            updateRefreshToken(user, oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2UserRequest.getAccessToken());
        } catch (Exception ex){
            Logger.getAnonymousLogger().log(Level.WARNING, null, ex);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private RefreshToken updateRefreshToken(User user, String getRegistrationId, OAuth2AccessToken getAccessToken) throws ExecutionException, InterruptedException {
        OAuth2RefreshToken refreshToken = getRefreshToken(getRegistrationId, getAccessToken);
        return persistRefreshToken(user, refreshToken);
    }

    private OAuth2RefreshToken getRefreshToken(String getRegistrationId, OAuth2AccessToken getAccessToken) throws ExecutionException, InterruptedException {
        return OAuth2RefreshTokenFactory.getOAuth2RefreshToken(getRegistrationId, getAccessToken).exchangeAccessTokenForRefreshToken().getRefreshToken();
    }

    private RefreshToken persistRefreshToken(User user, OAuth2RefreshToken oAuth2RefreshToken) {
        RefreshToken refreshToken = new RefreshToken(oAuth2RefreshToken);
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}
