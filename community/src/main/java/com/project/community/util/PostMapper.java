package com.project.community.util;

import com.project.community.dto.PostResponseDto;
import com.project.community.dto.request.PostRequest;
import com.project.community.entity.Post;
import com.project.community.entity.User;

public class PostMapper {
    public static Post toPost(PostRequest postRequest, User writer) {
        return new Post(postRequest.getTitle(), postRequest.getContents(), writer);
    }

    public static PostResponseDto toResponseDto(Post post) {
        return new PostResponseDto(post.getId(), post.getTitle(), post.getContents(), UserMapper.toResponseDto(post.getWriter()), post.getCreatedAt(), post.getLikesCount());
    }
}
