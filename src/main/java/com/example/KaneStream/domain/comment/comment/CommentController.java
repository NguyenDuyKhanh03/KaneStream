package com.example.KaneStream.domain.comment.comment;

import com.example.KaneStream.domain.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/gets/comments")
    public ResponseEntity<Page<CommentDto>> getComments(@RequestParam(defaultValue = "0") int page,@RequestParam UUID postId) {
        Page<CommentDto> comments=commentService.getComments(page,postId);
        return ResponseEntity.ok(comments);
    }

    @MessageMapping("/comment/post/{postId}")
    @SendTo("/topic/comment/post/{postId}")
    public CommentDto postComment(@DestinationVariable UUID postId, @Payload String content, Principal principal) {
        String username=principal.getName();
        return commentService.postComment(postId,content,username);
    }

    @MessageMapping("/comment/post/reply/{commentId}")
    @SendTo("/topic/comment/post/reply/{postId}")
    public CommentDto postCommentReply(@DestinationVariable UUID postId,@DestinationVariable UUID commentId, @Payload String content) {
        return commentService.postCommentReply(commentId,content);
    }


    @MessageMapping("/comment/delete/{postId}")
    @SendTo("/topic/comment")
    public String deleteComment(@DestinationVariable UUID postId) {
        return commentService.deleteComment(postId);
    }
}
