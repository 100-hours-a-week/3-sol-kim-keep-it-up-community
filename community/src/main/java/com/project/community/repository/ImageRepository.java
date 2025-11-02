package com.project.community.repository;

import com.project.community.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByTypeAndUserId(String profile, Long userId);

    Image findByTypeAndPostId(String post, Long postId);

    List<Image> findAllByTypeAndUserId(String profile, Long userId);
}
