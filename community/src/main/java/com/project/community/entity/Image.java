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
    String filename;

    String originalFilename;

    Long size;

    Long userId;

    Long postId;

    String type;

    public Image(String filename, String originalFilename, Long size, Long userId, String type) {
        this.filename = filename;
        this.originalFilename = originalFilename;
        this.size = size;
        this.userId = userId;
        this.type = type;
    }
}
