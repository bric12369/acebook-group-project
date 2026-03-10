package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    //private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id") // foreign key
    private User user;

    public Post() {}

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
        //this.timestamp = LocalDateTime.now();
    }
}