package com.project.community.service;

import com.project.community.common.CustomException;
import com.project.community.common.ErrorCode;
import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.dto.request.PostUpdateRequest;
import com.project.community.entity.Post;
import com.project.community.entity.User;
import com.project.community.repository.PostRepository;
import com.project.community.repository.UserRepository;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.community.util.PostMapper;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    /*
    게시글 작성(V2)
     */
    @Transactional
    public PostResponseDto createPost(HttpServletRequest request, PostRequest postRequestDto) {
        HttpSession session = request.getSession(false);
        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);
        Long userId = (Long) session.getAttribute("userId");

        User writer = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = PostMapper.toPost(postRequestDto, writer);
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }

    /*
    게시글 상세 조회
     */
    public PostResponseDto getPostDetail(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        return PostMapper.toResponseDto(post);
    }

    /*
    게시글 목록 조회
     */
    public Slice<PostResponseDto> getPostList(@Nullable Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(desc("id")));
        Slice<Post> slice;
        if (cursorId == null) {
            slice = postRepository.findFirstSlice(pageable);
        } else {
            slice = postRepository.findNextSlice(cursorId, pageable);
        }

        return  slice.map(PostMapper::toResponseDto);
    }

    /*
    게시글 수정(V1)
     */
    @Transactional
    public PostResponseDto updatePost(Long id, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        post.setTitle(postUpdateRequest.getTitle());
        post.setContents(postUpdateRequest.getContents());
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }

    /*
    게시글 수정(V2)
     */
    @Transactional
    public PostResponseDto updatePost(HttpServletRequest request, Long postId, PostUpdateRequest postUpdateRequest) {
        HttpSession session = request.getSession(false);
        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);

        Long userId = (Long) session.getAttribute("userId");

        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getWriter().getId().equals(userId)) throw new CustomException(ErrorCode.WRITER_ONLY_EDIT);
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        post.setTitle(postUpdateRequest.getTitle());
        post.setContents(postUpdateRequest.getContents());
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }

    /*
    게시글 삭제(V1)
     */
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        post.setDeleted(true);
        postRepository.save(post);
    }

    /*
    게시글 삭제(V2)
     */
    @Transactional
    public void deletePost(HttpServletRequest request, Long postId) {
        HttpSession session = request.getSession(false);
        if (session == null) throw new CustomException(ErrorCode.SIGNIN_NEEDED);

        Long userId = (Long) session.getAttribute("userId");

        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getWriter().getId().equals(userId)) throw new CustomException(ErrorCode.WRITER_ONLY_DELETE);
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        post.setDeleted(true);
        postRepository.save(post);
    }

    /*
    조회수 증가
     */
    @Transactional
    public PostResponseDto increaseViewsCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.isDeleted()) throw new CustomException(ErrorCode.POST_NOT_FOUND);
        post.increaseViewsCount();
        postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }
}
