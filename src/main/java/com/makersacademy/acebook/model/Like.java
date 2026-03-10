package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name="LIKES")
@Getter @Setter @NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer likes;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    public Like(Integer likes, Long post_id, Long user_id) {
        this.likes = likes;
        this.postId = post_id;
        this.userId = user_id;
    }
}
