package com.example.KaneStream.domain.post.post;

import com.example.KaneStream.domain.topic.Topic;
import com.example.KaneStream.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    private String image;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "liked_count")
    private int likedCount;

    @Column(name = "comments_count")
    private int commentsCount;

    @Column(name = "is_featured")
    private int isFeatured;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;




}
