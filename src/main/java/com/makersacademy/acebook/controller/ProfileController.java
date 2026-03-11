package com.makersacademy.acebook.controller;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/users/{id}")
    public String showProfile(@PathVariable Long id, Model model, @AuthenticationPrincipal DefaultOidcUser oidcUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postRepository.findByUser_Id(id);

        // Get logged-in user from DB
        User currentUser = userRepository.findUserByUsername(oidcUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        boolean isCurrentUser = currentUser.getId().equals(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("isCurrentUser", isCurrentUser);

        return "profile/userprofile";
    }

    @GetMapping("/users/{id}/edit")
    public String editProfile(@PathVariable Long id, Model model, @AuthenticationPrincipal DefaultOidcUser oidcUser){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        User currentUser = userRepository.findUserByUsername(oidcUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        if (!currentUser.getId().equals(user.getId())) {
            return "redirect:/users/" + id; // prevent editing others
        }

        model.addAttribute("user", user);
        return "profile/editprofile";
    }

    @PostMapping("/users/{id}/edit")
    public RedirectView updateProfile(
            @PathVariable Long id,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String displayName) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setDisplayName(displayName);
        user.setBio(bio);

        userRepository.save(user);

        return new RedirectView("/users/" + id);
    }


}
