package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Notification;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.NotificationRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin() {

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");

        userRepository
                .findUserByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User(username);
                    newUser.setDisplayName(username);
                    newUser.setEnabled(true);
                    newUser.setProfileImageUrl("avatar_current_user.jpeg");
                    User savedUser = userRepository.save(newUser);

                    notificationRepository.save(new Notification(savedUser, "Welcome to Acebook!"));
                    notificationRepository.save(new Notification(savedUser, "You have a new friend suggestion!"));
                    notificationRepository.save(new Notification(savedUser, "Someone liked your post!"));

                    return savedUser;

                });

        return new RedirectView("/posts");
    }
}
