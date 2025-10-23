package com.project.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    Long id;
    String path;

    String filename;

    Long size;

    Long userId;

    Long postId;

    public Image(String path, String filename, Long size) {
        this.path = path;
        this.filename = filename;
        this.size = size;
    }
}
