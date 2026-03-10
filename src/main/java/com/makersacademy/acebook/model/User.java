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

    private boolean enabled;

    // Optional: link posts to user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    public User() {}

    public User(String username) {
        this.username = username;
        this.enabled = true; // default enabled
    }

    public User(String username, boolean enabled) {
        this.username = username;
        this.enabled = enabled;
    }
}