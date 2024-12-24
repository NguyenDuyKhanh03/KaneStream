package com.example.KaneStream.domain.post.post_like;

import com.example.KaneStream.domain.post.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostService postService;

    @MessageMapping("/post/{postId}/like")
    @SendTo("/topic/posts")
    public PostLikeResponse handleLikePost(@DestinationVariable UUID postId, Principal principal) {
        String username = principal.getName();
        int likedCount= postService.updateLikeCount(postId,username);
        return new PostLikeResponse(postId, likedCount);
    }


}
