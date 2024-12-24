package com.example.KaneStream.domain.comment.comment;

import com.example.KaneStream.domain.comment.CommentStatus;
import com.example.KaneStream.domain.post.post.Post;
import com.example.KaneStream.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    private String content;

    @Column(name="liked_count")
    private int likedCount;

    @Column(name="reply_count")
    private int replyCount;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::comments_type_enum")
    private CommentStatus status;

    @Column(name="created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name="updated_at")
    @CreationTimestamp
    private Timestamp updatedAt;

}
