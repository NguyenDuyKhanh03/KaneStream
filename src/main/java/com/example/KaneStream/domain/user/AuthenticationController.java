package com.example.KaneStream.domain.user;

import com.example.KaneStream.domain.user.constant.Constant;
import com.example.KaneStream.domain.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterAccountRequest request){
        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return service.login(request);
    }






}
