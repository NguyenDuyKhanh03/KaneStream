package com.example.KaneStream.domain.post.post;

import com.example.KaneStream.domain.user.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostResponse {
    private UserDto user;
    private PostDto post;
}
