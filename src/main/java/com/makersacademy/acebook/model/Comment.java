package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name="COMMENTS")
@Getter @Setter @NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Long post_id;
    private Long user_id;

    public Comment(String content, Long post_id, Long user_id) {
        this.content = content;
        this.post_id = post_id;
        this.user_id = user_id;
    }
}
