package com.makersacademy.acebook.controller;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String showProfile(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postRepository.findByUser_Id(id);

        // Determine if current user is viewing own profile
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isCurrentUser = currentUsername.equals(user.getUsername());


        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("isCurrentUser", isCurrentUser);

        return "profile/profile";

    }
}
