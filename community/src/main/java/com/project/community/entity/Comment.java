package com.project.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String contents;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Comment(String contents, User writer, Post post) {
        this.contents = contents;
        this.writer = writer;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }
}
