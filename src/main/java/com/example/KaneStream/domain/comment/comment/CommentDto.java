package com.example.KaneStream.domain.comment.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String avatar;
    private String username;
    private UUID id;
    private String content;
    private int likedCount;
    private UUID userId;
    private UUID postId;
}
