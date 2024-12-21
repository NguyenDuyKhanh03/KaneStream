package com.example.KaneStream.domain.comment.comment_like;

import com.example.KaneStream.domain.comment.CommentService;
import com.example.KaneStream.domain.comment.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentService commentService;


    @MessageMapping("/comment/{commentId}")
    @SendTo("/topic/comment")
    public CommentDto updateCommentLike(@DestinationVariable UUID commentId) {
        return commentService.updateCommentLike(commentId);
    }

}
