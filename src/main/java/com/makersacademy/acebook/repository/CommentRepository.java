package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Iterable<Comment> getCommentsByPostId(Long postId);

    Iterable<Comment> getCommentsByUserId(Long userId);
}
