package com.example.KaneStream.domain.user.controller;

import com.example.KaneStream.constant.Constant;
import com.example.KaneStream.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam MultipartFile file){
        userService.uploadAvatar(file);
        return ResponseEntity.ok().body(Constant.STATUS_SUCCESS);
    }

    @PatchMapping("/upload-cover")
    public ResponseEntity<String> uploadCover(@RequestParam MultipartFile file){
        userService.uploadCover(file);
        return ResponseEntity.ok().body(Constant.STATUS_SUCCESS);
    }
}
