package com.example.KaneStream.domain.user.service;

import com.example.KaneStream.domain.user.UserDto;
import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.domain.user.UserRepository;
import com.example.KaneStream.exeption.ResourceNotFoundException;
import com.example.KaneStream.integration.minio.MinioChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MinioChannel minioChannel;

    public Optional<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if(auth != null && auth.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails)auth.getPrincipal()).getUsername();
        }
        return userRepository.findByUsername(username);
    }

    public void uploadAvatar(MultipartFile file) {
        User user= getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        user.setAvatar(minioChannel.upload(file));
        userRepository.save(user);
    }


    public void uploadCover(MultipartFile file) {
        User user= getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        user.setAvatar(minioChannel.upload(file));
        userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public UserDto getUser(){
        User user= getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return new UserDto(user.getId(),user.getLastName(),user.getAvatar(),user.getUsername(),user.getBio(),user.getPostsCount(),user.getFollowersCount(),0,"123" );

    }
}
