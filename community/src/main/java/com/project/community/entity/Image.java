package com.project.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    Long id;
    String url;

    Long userId;

    Long postId;

    String type;

    public Image(String url, Long id, String type) {
        if (type.equals("post")) {
            this.url = url;
            this.postId = id;
            this.type = type;
        } else {
            this.url = url;
            this.userId = id;
            this.type = type;
        }
    }
}
