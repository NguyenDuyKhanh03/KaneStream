package com.example.KaneStream.domain.follower;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/follower")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping("create")
    public ResponseEntity<String> createFollow(@RequestParam UUID followingId) {
        return followerService.createFollowing(followingId);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowFollow(@RequestParam UUID followingId) {
        return followerService.unfollow(followingId);
    }
}
