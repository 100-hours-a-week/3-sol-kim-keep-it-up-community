package com.project.community.repository;

import com.project.community.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByTypeAndUserId(String profile, Long userId);

    Image findByTypeAndPostId(String post, Long postId);
}
