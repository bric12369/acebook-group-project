package com.makersacademy.acebook.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts")
    public String index(Model model, @AuthenticationPrincipal DefaultOidcUser oidcUser) {
        List<Post> posts = repository.findByOrderByCreatedAtDesc();

        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());

        if(oidcUser != null) {
            String email = oidcUser.getEmail();
            Optional<User> currentUser = userRepository.findUserByUsername(email);
            currentUser.ifPresent(user -> model.addAttribute("currentUserId", user.getId()));
        }

        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String getPostById(Model model, @PathVariable Long id) {
        Optional<Post> post = repository.findById(id);
        model.addAttribute("post", post.get());
        return "posts/post";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post) {
        post.setCreatedAt(LocalDateTime.now());
        repository.save(post);
        return new RedirectView("/posts");
    }

    @PostMapping("/comments")
    public RedirectView postComment(@RequestParam Long postId, @RequestParam String content, @AuthenticationPrincipal DefaultOidcUser oidcUser){
        Optional<User> user = userRepository.findUserByUsername(oidcUser.getEmail());
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPostId(postId);
        comment.setUserId(user.get().getId());
        commentRepository.save(comment);
        return new RedirectView("/posts");
    }
}
