package com.example.KaneStream.domain.post.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostRequest {
    private int page;
    private int size;
    private String sortBy;
}
