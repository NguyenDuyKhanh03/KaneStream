package com.example.KaneStream.domain.post.post;

import com.example.KaneStream.domain.user.UserDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UserDto user;
    private PostDto post;


}
