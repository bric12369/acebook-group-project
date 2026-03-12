package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Follow;
import com.makersacademy.acebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    long countByFollowing(User user); // followers
    long countByFollower(User user); //following

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
