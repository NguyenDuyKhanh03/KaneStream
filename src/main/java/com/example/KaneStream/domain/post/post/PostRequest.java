package com.example.KaneStream.domain.post.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String content;
    private MultipartFile file;
    private UUID topic;
}
