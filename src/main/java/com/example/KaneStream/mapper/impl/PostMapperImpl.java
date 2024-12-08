package com.example.KaneStream.mapper.impl;

import com.example.KaneStream.domain.post.post.Post;
import com.example.KaneStream.domain.post.post.PostDto;
import com.example.KaneStream.domain.post.post.PostRepository;
import com.example.KaneStream.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapperImpl implements Mapper<Post, PostDto> {

    private final ModelMapper modelMapper;

    @Override
    public PostDto mapFrom(Post post) {
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public Post mapTo(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }
}
