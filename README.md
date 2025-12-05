# Keepit-up 킵잇업 (Backend)

> 목표 달성을 위한 커뮤니티 서비스 **Keepit-Up**의 백엔드 서버 레포지토리입니다.

---

## 📌 프로젝트 개요

- **서비스 설명**:  
  - 예) 사용자가 목표를 등록하고, 게시글/댓글로 서로 응원하는 커뮤니티 서비스의 REST API 서버입니다.
- **주요 기능**
  - 회원가입 / 로그인 (JWT)
  - 회원 정보 수정 / 비밀번호 수정 / 회원 탈퇴 
  - 게시글 CRUD
  - 댓글 CRUD / 좋아요
  - 이미지 업로드 (S3 Presigned URL)
---

## 🏗 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Build Tool**: Gradle
- **Database**:  MySQL (prod), H2 (test)
- **ORM**: Spring Data JPA / Hibernate
- **Security**: BCrypt(비밀번호 암호화, Spring Security), JWT (jjwt), 
- **Infra / DevOps**
  - AWS EC2, S3, Lambda, API Gateway, VPC
  - Docker, Docker Compose
  - GitHub Actions, Jenkins
- **Test**: Spring Boot Test, Spring Security Test, JUnit 5
- **Etc**
  - Lombok
---

## 🧱 아키텍처 / 구조

### 🪜 시스템 아키텍처

<img width="1000" alt="Group 53 (1)" src="https://github.com/user-attachments/assets/def3671e-ed45-4b9f-b9ea-b8f43cd8e328" />


- Client → Nginx → Frontend → Backend(API) → DB

### 🗂️ 패키지 구조

```text
├── README.md
├── community
│   ├── HELP.md
│   ├── build
│   ├── build.gradle
│   ├── dockerfile
│   ├── gradle
│   │   └── wrapper
│   │       ├── gradle-wrapper.jar
│   │       └── gradle-wrapper.properties
│   ├── gradlew
│   ├── gradlew.bat
│   ├── settings.gradle
│   └── src
│       ├── main
│       │   ├── java/com/project/community
│       │   │               ├── CommunityApplication.java
│       │   │               ├── common
│       │   │               │   ├── ControllerAdvice.java
│       │   │               │   ├── CustomException.java
│       │   │               │   ├── ErrorCode.java
│       │   │               │   ├── ErrorMessage.java
│       │   │               │   ├── ErrorResponse.java
│       │   │               │   └── Message.java
│       │   │               ├── config
│       │   │               │   ├── CorsConfig.java
│       │   │               │   ├── SecurityConfig.java
│       │   │               │   ├── SessionInterceptor.java
│       │   │               │   ├── WebConfig.java
│       │   │               │   └── WebFilterConfig.java
│       │   │               ├── controller
│       │   │               │   ├── CommentController.java
│       │   │               │   ├── ImageController.java
│       │   │               │   ├── LegalController.java
│       │   │               │   ├── PostController.java
│       │   │               │   ├── PostLikeController.java
│       │   │               │   └── UserController.java
│       │   │               ├── dto
│       │   │               │   ├── CommentResponseDto.java
│       │   │               │   ├── ImagePostResponseDto.java
│       │   │               │   ├── ImageResponseDto.java
│       │   │               │   ├── PostLikeResponseDto.java
│       │   │               │   ├── PostResponseDto.java
│       │   │               │   ├── TokenResponseDto.java
│       │   │               │   ├── UserProfileResponseDto.java
│       │   │               │   ├── UserResponseDto.java
│       │   │               │   ├── request
│       │   │               │   └── response
│       │   │               ├── entity
│       │   │               │   ├── Comment.java
│       │   │               │   ├── Image.java
│       │   │               │   ├── Post.java
│       │   │               │   ├── PostLike.java
│       │   │               │   ├── RefreshToken.java
│       │   │               │   └── User.java
│       │   │               ├── filter
│       │   │               │   └── JwtFilter.java
│       │   │               ├── repository
│       │   │               │   ├── CommentRepository.java
│       │   │               │   ├── ImageRepository.java
│       │   │               │   ├── PostLikeRepository.java
│       │   │               │   ├── PostRepository.java
│       │   │               │   ├── RefreshTokenRepository.java
│       │   │               │   └── UserRepository.java
│       │   │               ├── service
│       │   │               │   ├── CommentService.java
│       │   │               │   ├── ImageService.java
│       │   │               │   ├── PostLikeService.java
│       │   │               │   ├── PostService.java
│       │   │               │   └── UserService.java
│       │   │               └── util
│       │   │                   ├── CommentMapper.java
│       │   │                   ├── ErrorResponseWriter.java
│       │   │                   ├── ImageMapper.java
│       │   │                   ├── JwtProperties.java
│       │   │                   ├── JwtUtil.java
│       │   │                   ├── PostMapper.java
│       │   │                   └── UserMapper.java
│       │   └── resources
│       │       ├── application-local.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── static
│       │       └── templates
│       │           └── legal
│       │               ├── privacy.html
│       │               └── terms.html
│       └── test
│           ├── java/com/project/community
│           │               ├── CommunityApplicationTests.java
│           │               ├── entity
│           │               │   └── PostTest.java
│           │               └── service
│           │                   ├── CommentServiceTest.java
│           │                   ├── PostServiceTest.java
│           │                   └── UserServiceTest.java
│           └── resources
│               └── application.yml
└── deploy.sh
```

## ✅ 컨벤션
## 🧑‍💻 커밋 컨벤션
```
feat:    새로운 기능 추가
fix:     버그 수정
docs:    문서 수정
refactor: 코드 구조 개선
chore:   설정 파일 수정 등
```
