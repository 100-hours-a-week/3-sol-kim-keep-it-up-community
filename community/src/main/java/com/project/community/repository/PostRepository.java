package com.project.community.repository;

import com.project.community.entity.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
              SELECT p FROM Post p
              WHERE p.isDeleted = false
              ORDER BY p.id DESC
            """)
    Slice<Post> findFirstSlice(Pageable pageable);

    @Query("""
              SELECT p FROM Post p
              WHERE p.isDeleted = false
                AND (p.id < :cursorId)
              ORDER BY p.id DESC
            """)
    Slice<Post> findNextSlice(@Param("cursorId") Long cursorId, Pageable pageable);
}
