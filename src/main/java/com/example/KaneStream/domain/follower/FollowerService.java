package com.example.KaneStream.domain.follower;

import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.domain.user.service.UserService;
import com.example.KaneStream.exeption.ResourceAlreadyExistsException;
import com.example.KaneStream.exeption.ResourceNotFoundException;
import com.example.KaneStream.exeption.UserNotAuthenticatedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowerService {
    private final FollowerRepository followerRepository;
    private final UserService userService;

    @Transactional
    public ResponseEntity<String> createFollowing(UUID followingId) {
        User user=userService.getCurrentUser()
                .orElseThrow(() -> new UserNotAuthenticatedException("User not authenticated."));

        User following= userService.getUserById(followingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found.")
                );

        Follower follower=followerRepository.findById(new FollowerId(user.getId(),followingId)).orElse(null);

        if(!Objects.isNull(follower)) {
            throw new ResourceAlreadyExistsException("Follower already exists.");
        }


        FollowerId followerId=new FollowerId();
        followerId.setFollowingId(followingId);
        followerId.setFollowerId(user.getId());

        follower=new Follower();
        follower.setId(followerId);

        followerRepository.save(follower);

        following.setFollowersCount(following.getFollowersCount()+1);
        userService.updateUser(following);
        return ResponseEntity.ok("Follower created successfully.");
    }

    public ResponseEntity<String> unfollow(UUID followingId) {
        User user=userService.getCurrentUser()
                .orElseThrow(() -> new UserNotAuthenticatedException("User not authenticated."));

        User following= userService.getUserById(followingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found.")
                );
        Follower follower=followerRepository.findById(new FollowerId(user.getId(),followingId))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Follower not found.")
                );

        followerRepository.delete(follower);

        following.setFollowersCount(following.getFollowersCount()-1);
        userService.updateUser(following);

        return ResponseEntity.ok("Follower undeleted successfully.");

    }
}
