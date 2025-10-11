package com.project.community.repository;

import com.project.community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    PostLike findByUserIdAndPostId(Long userId, Long postId);
}
