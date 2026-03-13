package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Like;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPostId(Long postId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    @Transactional
    void deleteByPostIdAndUserId(Long postId, Long userId);

    List<Like> findByUserId(long userId);

    @Query(value = "select post_id from likes where user_id = ?1", nativeQuery = true)
    Set<Long> findPostIdsByUserId(Long userId);
}
