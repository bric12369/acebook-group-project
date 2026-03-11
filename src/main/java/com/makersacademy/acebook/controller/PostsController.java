package com.makersacademy.acebook.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.LikeRepository;
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

    @Autowired
    LikeRepository likeRepository;

    @GetMapping("/posts")
    public String index(Model model) {
        List<Post> posts = repository.findByOrderByCreatedAtDesc();

        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String getPostById(Model model, @PathVariable Long id) {
        Optional<Post> post = repository.findById(id);
        Iterable<Comment> comments = commentRepository.getCommentsByPostId(id);
        model.addAttribute("post", post.get());
        model.addAttribute("comments", comments);
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
        comment.setUser(user.get());
        commentRepository.save(comment);
        return new RedirectView("/posts");
    }
}
