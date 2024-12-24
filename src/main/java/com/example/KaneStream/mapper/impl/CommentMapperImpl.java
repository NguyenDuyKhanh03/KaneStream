package com.example.KaneStream.mapper.impl;

import com.example.KaneStream.domain.comment.comment.Comment;
import com.example.KaneStream.domain.comment.comment.CommentDto;
import com.example.KaneStream.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapperImpl implements Mapper<Comment, CommentDto> {
    private final ModelMapper modelMapper;
    @Override
    public CommentDto mapFrom(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        commentDto.setUsername(comment.getUser().getUsername());
        commentDto.setAvatar(comment.getUser().getAvatar());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setUserId(comment.getUser().getId());
        return commentDto;
    }

    @Override
    public Comment mapTo(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}
