package com.example.KaneStream.domain.comment.comment;

import com.example.KaneStream.domain.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/gets/page")
    public ResponseEntity<Page<CommentDto>> getComments(@RequestParam(defaultValue = "0") int page) {
        Page<CommentDto> comments=commentService.getComments(page);
        return ResponseEntity.ok(comments);
    }

    @MessageMapping("/comment/post/{postId}")
    @SendTo("/topic/comment")
    public CommentDto postComment(@DestinationVariable UUID postId, @Payload String content) {
        return commentService.postComment(postId,content);
    }

    @MessageMapping("/comment/delete/{postId}")
    @SendTo("/topic/comment")
    public String deleteComment(@DestinationVariable UUID postId) {
        return commentService.deleteComment(postId);
    }
}
