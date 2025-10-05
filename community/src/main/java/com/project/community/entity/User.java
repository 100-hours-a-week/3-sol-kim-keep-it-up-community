package com.project.community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id // PK(기본키) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : Id가 null일 경우 해당 객체의 Id를 DB의 자동 증가 컬럼으로 가져와 할당(DB 벤더에 의존적, 기본키 생성을 DB에게 위임, 유일성 보장)
    @Column(name = "comment_id") // 데이터베이스에 어떤 이름과 형식으로 저장될지 지정. 이름, 데이터 형식, 제약 조건 등을 명시
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String nickname;

    @Email(message = "이메일 형식이 아닙니다.")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // private String profilImageUrl;

    @OneToMany(mappedBy = "writer")
    private List<Post> postList = new ArrayList<>();

    private boolean didWithdraw = false;

    public User(String nickname, String email, String encryptedPassword) {
        this.nickname = nickname;
        this.email = email;
        password = encryptedPassword;
    }
}
