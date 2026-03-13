package com.makersacademy.acebook.controller;

import java.time.LocalDateTime;
import java.util.*;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

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
    public String index(Model model, @AuthenticationPrincipal DefaultOidcUser oidcUser) {
        List<Post> posts = repository.findByOrderByCreatedAtDesc();

        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());

        if(oidcUser != null) {
            String email = oidcUser.getEmail();
            Optional<User> currentUser = userRepository.findUserByUsername(email);
            currentUser.ifPresent(user -> model.addAttribute("currentUserId", user.getId()));
            Set<Long> likedPosts = likeRepository.findPostIdsByUserId(currentUser.get().getId());
            model.addAttribute("likedPosts", likedPosts);
            model.addAttribute("user", currentUser.get());
            model.addAttribute("isCurrentUser", true);

        }


        Map<Long, Long> likeCounts = new HashMap<>();

        for (Post post : posts) {
            likeCounts.put(post.getId(), likeRepository.countByPostId(post.getId()));
        }

        model.addAttribute("likeCounts", likeCounts);

        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String getPostById(Model model, @PathVariable Long id, @AuthenticationPrincipal DefaultOidcUser oidcUser) {
        Optional<Post> post = repository.findById(id);
        Iterable<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(id);
        Optional<User> currentUser = userRepository.findUserByUsername(oidcUser.getEmail());
        Set<Long> likedPosts = likeRepository.findPostIdsByUserId(currentUser.get().getId());

        Map<Long, Long> likeCounts = new HashMap<>();
        likeCounts.put(id, likeRepository.countByPostId(id));

        model.addAttribute("likeCounts", likeCounts);
        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("post", post.get());
        model.addAttribute("comments", comments);
        return "posts/post";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post, @AuthenticationPrincipal DefaultOidcUser oidcUser) {
        post.setCreatedAt(LocalDateTime.now());

        Optional<User> user = userRepository.findUserByUsername(oidcUser.getEmail());

        post.setUser(user.get());
        repository.save(post);
        return new RedirectView("/posts");
    }

    @DeleteMapping("/posts/{id}")
    public RedirectView deletePost(@PathVariable Long id, @RequestParam String redirect, @AuthenticationPrincipal DefaultOidcUser oidcUser) {
        Optional<User> user = userRepository.findUserByUsername(oidcUser.getEmail());
        Optional<Post> post = repository.findById(id);
        if (Objects.equals(user.get().getId(), post.get().getUser().getId())) {
            repository.deleteById(id);
        }
        return new RedirectView(redirect);
    }

    @PostMapping("/comments")
    public RedirectView postComment(@RequestParam Long postId, @RequestParam String content, @AuthenticationPrincipal DefaultOidcUser oidcUser){
        Optional<User> user = userRepository.findUserByUsername(oidcUser.getEmail());
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPostId(postId);
        comment.setUser(user.get());
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return new RedirectView("/posts/" + postId);
    }

    @PostMapping("/posts/{postId}/like")
    public RedirectView toggleLike(@PathVariable Long postId,
                                   @AuthenticationPrincipal DefaultOidcUser oidcUser) {

        Optional<User> user = userRepository.findUserByUsername(oidcUser.getEmail());
        Long userId = user.get().getId();

        boolean alreadyLiked = likeRepository.existsByPostIdAndUserId(postId, userId);

        if (alreadyLiked) {
            likeRepository.deleteByPostIdAndUserId(postId, userId);
        } else {
            Like like = new Like(postId, userId);
            likeRepository.save(like);
        }

        return new RedirectView("/posts");
    }
}
