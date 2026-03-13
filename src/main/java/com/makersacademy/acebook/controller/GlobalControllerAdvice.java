package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Notification;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FollowRepository;
import com.makersacademy.acebook.repository.NotificationRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    // Notifications for navbar
    @ModelAttribute("notifications")
    public List<Notification> addNotifications(@AuthenticationPrincipal DefaultOidcUser oidcUser) {
        if (oidcUser == null) return List.of();

        User currentUser = userRepository.findUserByUsername(oidcUser.getEmail())
                .orElse(null);

        if (currentUser == null) return List.of();

        return notificationRepository.findByUser(currentUser);
    }

    // Current logged in user ID for navbar / links
    @ModelAttribute("currentUserId")
    public Long addCurrentUserId(@AuthenticationPrincipal DefaultOidcUser oidcUser) {
        if (oidcUser == null) return null;

        return userRepository.findUserByUsername(oidcUser.getEmail())
                .map(User::getId)
                .orElse(null);
    }

    // Follower count
    @ModelAttribute("followersCount")
    public long addFollowersCount(@AuthenticationPrincipal DefaultOidcUser oidcUser) {
        if (oidcUser == null) return 0;

        return userRepository.findUserByUsername(oidcUser.getEmail())
                .map(user -> followRepository.countByFollowing(user))
                .orElse(0L);
    }

    // Following count
    @ModelAttribute("followingCount")
    public long addFollowingCount(@AuthenticationPrincipal DefaultOidcUser oidcUser) {
        if (oidcUser == null) return 0;

        return userRepository.findUserByUsername(oidcUser.getEmail())
                .map(user -> followRepository.countByFollower(user))
                .orElse(0L);
    }
}