package com.example.KaneStream.domain.post.post_tag;

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
@Table(name = "posts_tags")
public class PostTag {

    @EmbeddedId
    private PostTagId id;

//    @Column(name = "post_id")
//    @MapsId("postId")
//    private UUID postId;
//
//    @Column(name = "tag_id")
//    @MapsId("tagId")
//    private UUID tagId;

    @Column(name = "created_at")
    @CreatedDate
    private Timestamp createdAt;
}
