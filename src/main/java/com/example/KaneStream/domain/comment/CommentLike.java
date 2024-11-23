package com.example.KaneStream.domain.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "comments_likes")
public class CommentLike {

    @EmbeddedId
    private CommentLikeId id;

    @MapsId("commentId")
    @Column(name = "comment_id")
    private UUID commentId;

    @MapsId("userId")
    @Column(name = "user_id")
    private UUID user;

    @Column(name = "created_at")
    @CreatedDate
    private Timestamp createdAt;
}
