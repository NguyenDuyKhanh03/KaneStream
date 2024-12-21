package com.example.KaneStream.domain.post.post_like;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "posts_likes")
public class PostLike {

    @EmbeddedId
    private PostLikeId id;

//    @Column(name = "post_id")
//    @MapsId("postId")
//    private UUID postId;
//
//    @Column(name = "user_id")
//    @MapsId("userId")
//    private UUID userId;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
}
