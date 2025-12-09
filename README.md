# Keepit-up 킵잇업 (Backend)

> 목표 달성을 위한 커뮤니티 서비스 **Keepit-Up**의 백엔드 서버 레포지토리입니다.


데모 영상: [https://www.notion.so/Keepit-up-2c41c972687480eeb11fd1012561225d?source=copy_link](https://www.notion.so/Keepit-up-2c41c972687480eeb11fd1012561225d?source=copy_link)

고민 과정 및 트러블 슈팅: [https://www.notion.so/Keepit-up-2c41c97268748025a76bf956a04baeeb?source=copy_link](https://www.notion.so/Keepit-up-2c41c97268748025a76bf956a04baeeb?source=copy_link)

---

## 📌 프로젝트 개요

- **서비스 설명**:  
  목표 혹은 수행한 TODO를 게시글로 공유하고 댓글로 서로를 응원하는 커뮤니티 서비스의 REST API 서버입니다.
- **주요 기능**
  - 회원가입 / 로그인 (JWT)
  - 회원 정보 수정 / 비밀번호 수정 / 회원 탈퇴 
  - 게시글 CRUD
  - 댓글 CRUD / 좋아요
  - 이미지 업로드 (S3 Presigned URL)
---

## 🏗 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.6
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

### 시스템 아키텍처
<img width="1000" height="963" alt="Group 53 (3)" src="https://github.com/user-attachments/assets/b50ac598-48a5-4591-874b-8bc74c87789b" />

- Client → Nginx → Frontend → Backend(API) → DB

### 패키지 구조

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
│       │   │               ├── config
│       │   │               ├── controller
│       │   │               ├── dto
│       │   │               ├── entity
│       │   │               ├── filter
│       │   │               ├── repository
│       │   │               ├── service
│       │   │               └── util
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
### 커밋 컨벤션
```
feat:    새로운 기능 추가
fix:     버그 수정
docs:    문서 수정
refactor: 코드 구조 개선
chore:   설정 파일 수정 등
```

## ⚙️ 환경 설정

- `application.yml`        : 공통 설정
- `application-local.yml`  : 로컬 개발 환경
- `application-prod.yml`   : 운영/배포 환경

### 주요 환경 변수

| 이름           | 설명                   |
|----------------|------------------------|
| `DB_HOST`      | DB 호스트 (RDS 주소 등) |
| `DB_USER`      | DB 유저명              |
| `DB_PASSWORD`  | DB 비밀번호            |
| `JWT_SECRET`   | JWT 서명용 시크릿 키   |


## 🚀 실행 방법
- 로컬 실행 (local 프로파일)
```bash
cd community
./gradlew clean build
java -jar build/libs/community-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```
- 빌드 후 실행 (prod 프로파일)
```bash
cd community
./gradlew clean build
java -jar build/libs/community-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```
