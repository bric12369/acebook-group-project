package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "USERS") // Matches your database table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String displayName;
    private String profileImageUrl;

    private String bio;

    private boolean enabled;

    // Optional: link posts to user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    public User() {}

    public User(String username) {
        this.username = username;
        this.enabled = true; // default enabled
        this.displayName = username;
        this.bio = bio;
    }

    public User(String username, boolean enabled, String bio, String displayName, String profileImageUrl) {
        this.username = username;
        this.enabled = enabled;
        this.displayName = displayName;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }
}