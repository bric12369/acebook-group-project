package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Like;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {
    long countByPostId(Long postId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    @Transactional
    void deleteByPostIdAndUserId(Long postId, Long userId);
}
