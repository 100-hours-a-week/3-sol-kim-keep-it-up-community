package com.project.community.service;

import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.CommentRepository;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PostRepository postRepository = Mockito.mock(PostRepository.class);
    CommentRepository commentRepository = Mockito.mock(CommentRepository.class);

    CommentService commentService = new CommentService(
            commentRepository,
            postRepository,
            userRepository
    );

    @Test
    void registerComment() {

        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Long userId = 1L;
        Long postId = 1L;
        String contents = "오늘도 수고하셨습니다!";
        CommentPostRequest commentPostRequest = new CommentPostRequest(contents);

        Mockito.when(request.getAttribute("userId"))
                .thenReturn(1L);

        User writer = new User("Tim", "writer@writer.com", "encrypted-password");

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(writer));

        Post post = new Post("오늘 공부 기록", "디스패처 서블릿의 역할", writer);
        post.setId(1L);

        Mockito.when(postRepository.findById(postId))
                        .thenReturn(Optional.of(post));

        // when
        CommentResponseDto result = commentService.createComment(request, postId, commentPostRequest);

        //then
        Mockito.verify(commentRepository).save(Mockito.argThat(comment ->
                comment.getWriter().equals(writer)
                        && !comment.isDeleted()
        ));

        assertThat(result.getContents()).isEqualTo(contents);
        assertThat(result.getWriter().getNickname()).isEqualTo("Tim");
        assertThat(result.getPostId()).isEqualTo(postId);
    }
}
