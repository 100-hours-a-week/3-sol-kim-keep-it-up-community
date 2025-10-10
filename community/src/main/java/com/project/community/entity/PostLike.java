package com.project.community.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime createdAt;

    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
        createdAt = LocalDateTime.now();
    }
}

