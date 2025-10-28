package com.project.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
    private User writer;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private String imageUrl;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    @Where(clause = "is_deleted = false")
    private List<Comment> commentList = new ArrayList<>();

    private int likesCount = 0;

    private int viewsCount = 0;

    private int commentsCount = 0;

    public Post(String title, String contents, User writer) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
        commentsCount += 1;
    }

    public void deleteComment(Comment comment) {
        commentList.remove(comment);
        commentsCount -= 1;
    }

    public void increaseLikesCount() {
        likesCount += 1;
    }

    public void decreaseLikesCount() {
        likesCount -= 1;
    }

    public void increaseViewsCount() {
        viewsCount += 1;
    }
}
