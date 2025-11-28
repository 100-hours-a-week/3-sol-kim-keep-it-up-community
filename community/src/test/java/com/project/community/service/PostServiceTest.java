package com.project.community.service;

import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.entity.User;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);

    PostRepository postRepository = Mockito.mock(PostRepository.class);

    PostService postService = new PostService(userRepository, postRepository);


    @Test
    void publishPost() {
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String title = "스프링 공부와 헬스";
        String contents = "스프링 공부 - 프론트 컨트롤러 패턴: 톰캣은 웹서버를 내장하는 서블릿 컨테이너, 서블릿 엔진이다.";
        PostRequest postRequest = new PostRequest(title, contents);

        String nickname = "admin";
        String email = "admin@admin.com";
        String encryptedPassword = "encrypted-password";

        Long writerId = 1L;

        User writer = new User(nickname, email, encryptedPassword);
        writer.setId(writerId);

        Mockito.when(request.getAttribute("userId"))
                .thenReturn(writerId);

        Mockito.when(userRepository.findById(writerId))
                .thenReturn(Optional.of(writer));

        //when
        PostResponseDto result = postService.createPost(request, postRequest);

        //then
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContents()).isEqualTo(contents);
        assertThat(result.getWriter().getId()).isEqualTo(writerId);
        assertThat(result.getViewsCount()).isEqualTo(0);
        assertThat(result.getLikesCount()).isEqualTo(0);
        assertThat(result.getCommentsCount()).isEqualTo(0);
    }
}
