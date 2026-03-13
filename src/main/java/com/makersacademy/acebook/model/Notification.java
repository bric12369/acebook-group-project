package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Notification() {}

    public Notification(User user, String content) {
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}