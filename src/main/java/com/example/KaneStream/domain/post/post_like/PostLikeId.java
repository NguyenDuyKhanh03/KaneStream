package com.example.KaneStream.domain.post.post_like;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PostLikeId {
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "user_id")
    private UUID userId;

}
