package com.makersacademy.acebook.controller;
import com.makersacademy.acebook.model.Follow;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;

import com.makersacademy.acebook.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
public class ProfileController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/users/{id}")
    public String showProfile(@PathVariable Long id, Model model, @AuthenticationPrincipal DefaultOidcUser oidcUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postRepository.findByUser_Id(id);

        // Get logged-in user from DB
        User currentUser = userRepository.findUserByUsername(oidcUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        boolean isCurrentUser = currentUser.getId().equals(user.getId());

        long followersCount = followRepository.countByFollowing(user);
        long followingCount = followRepository.countByFollower(user);

        boolean isFollowing = followRepository
                .findByFollowerAndFollowing(currentUser, user)
                .isPresent();


        Set<Long> likedPosts = likeRepository.findPostIdsByUserId(currentUser.getId());

        Map<Long, Long> likeCounts = new HashMap<>();

        for (Post post : posts) {
            likeCounts.put(post.getId(), likeRepository.countByPostId(post.getId()));
        }

        model.addAttribute("likeCounts", likeCounts);
        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("followersCount", followersCount);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("isFollowing", isFollowing);

        return "profile/profile";
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

    @PostMapping("users/{id}/follow")
    public RedirectView followeruser(@PathVariable Long id, @AuthenticationPrincipal DefaultOidcUser oidcUser)
    {
        User toFollow = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = userRepository.findUserByUsername(oidcUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        if (!currentUser.getId().equals(toFollow.getId())) {

            followRepository.findByFollowerAndFollowing(currentUser, toFollow)
                    .orElseGet(() -> {
                        Follow follow = new Follow();
                        follow.setFollower(currentUser);
                        follow.setFollowing(toFollow);
                        return followRepository.save(follow);
                    });
        }

        return new RedirectView("/users/" + id);

    }
    @PostMapping("/users/{id}/unfollow")
    public RedirectView unfollowUser(@PathVariable Long id,
                                     @AuthenticationPrincipal DefaultOidcUser oidcUser) {

        User toUnfollow = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = userRepository.findUserByUsername(oidcUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        followRepository.findByFollowerAndFollowing(currentUser, toUnfollow)
                .ifPresent(followRepository::delete);

        return new RedirectView("/users/" + id);
    }

}
