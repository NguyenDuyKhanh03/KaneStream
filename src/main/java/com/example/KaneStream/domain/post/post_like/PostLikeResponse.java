package com.example.KaneStream.domain.post.post_like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponse {
    UUID postId;
    int likedCount;
}
