package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts")
    public String index(Model model) {
        // Iterable containing all posts
        Iterable<Post> posts = repository.findAll();
        // Convert Iterable to List
        List<Post> postsList = new ArrayList<>();
        posts.forEach(postsList::add);
        // Reverse the list to display newest posts first
        Collections.reverse(postsList);

        model.addAttribute("posts", postsList);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post) {
        repository.save(post);
        return new RedirectView("/posts");
    }


    @PostMapping("/comments")
    public RedirectView postComment(@RequestParam Long postId, @RequestParam String content){

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPostId(postId);
        comment.setUserId(1L);
        commentRepository.save(comment);
        return new RedirectView("/posts");
    }
}
