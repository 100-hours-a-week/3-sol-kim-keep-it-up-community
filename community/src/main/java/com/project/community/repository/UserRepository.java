package com.project.community.repository;

import com.project.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA
// JPA repository 상속 시 findAll, findById, save, delete 등의 기본 CRUD 메소드 제공
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByNicknameAndIsDeletedFalse(String nickname);

    boolean existsByNicknameAndIsDeletedFalse(String nickname);

    User findByEmail(String email);
}
