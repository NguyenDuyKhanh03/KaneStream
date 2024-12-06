package com.example.KaneStream.domain.user.service;

import com.example.KaneStream.domain.user.User;
import com.example.KaneStream.domain.user.UserRepository;
import com.example.KaneStream.integration.minio.MinioChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setAvatar(minioChannel.upload(file));
        userRepository.save(user);
    }

    public void uploadCover(MultipartFile file) {
        User user= getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setAvatar(minioChannel.upload(file));
        userRepository.save(user);
    }
}
