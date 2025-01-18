package com.example.KaneStream.domain.post.post;

import lombok.Data;

@Data
public class PostES {

    private String id;

    private String content;

    private String image;

    private String authorId; // Ánh xạ `User` thành ID (String)

    private String topicId; // Ánh xạ `Topic` thành ID (String)

}
