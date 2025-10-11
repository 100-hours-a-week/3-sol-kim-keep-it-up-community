package com.project.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id")
//    @Column(nullable = false)
    private User writer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    @Where(clause = "is_deleted = false")
    private List<Comment> commentList = new ArrayList<>();

    private int likesCount = 0;

    public Post(String title, String contents, User writer) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        createdAt = LocalDateTime.now();
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void deleteComment(Comment comment) {
        commentList.remove(comment);
    }

    public void increaseLikesCount() {
        likesCount += 1;
    }

    public void decreaseLikesCount() {
        likesCount -= 1;
    }
}
