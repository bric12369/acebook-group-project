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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @GetMapping("/users/search")
    public List<Map<String, Object>> searchUsers(@RequestParam String q) {

        List<User> users = userRepository.findByUsernameContainingIgnoreCase(q);

        List<Map<String, Object>> results = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("username", user.getUsername());
            results.add(map);
        }

        return results;
    }

}
