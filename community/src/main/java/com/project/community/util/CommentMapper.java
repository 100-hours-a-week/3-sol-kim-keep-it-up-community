package com.project.community.util;

import com.project.community.dto.CommentResponseDto;
import com.project.community.dto.request.CommentPostRequest;
import com.project.community.entity.Comment;
import com.project.community.entity.Post;
import com.project.community.entity.User;

public class CommentMapper {
    public static Comment toComment(User writer, Post post, CommentPostRequest commentPostRequest) {
        return new Comment(commentPostRequest.getContents(), writer, post);
    }

    public static CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), UserMapper.toResponseDto(comment.getWriter()), comment.getPost().getId(), comment.getContents(), comment.getCreatedAt());
    }
}
