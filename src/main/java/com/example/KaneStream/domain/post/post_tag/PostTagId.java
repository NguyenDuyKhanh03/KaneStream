package com.example.KaneStream.domain.post.post_tag;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PostTagId implements Serializable {
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "tag_id")
    private UUID tagId;
}
