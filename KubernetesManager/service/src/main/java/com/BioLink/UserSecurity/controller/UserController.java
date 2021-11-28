package com.BioLink.UserSecurity.controller;

import com.BioLink.UserSecurity.exception.ResourceNotFoundException;
import com.BioLink.UserSecurity.model.User;
import com.BioLink.UserSecurity.repository.UserRepository;
import com.BioLink.UserSecurity.security.CurrentUser;
import com.BioLink.UserSecurity.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
