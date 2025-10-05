package com.project.community.repository;

import com.project.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA
    // JPA repository 상속 시 findAll, findById, save, delete 등의 기본 CRUD 메소드 제공
}
