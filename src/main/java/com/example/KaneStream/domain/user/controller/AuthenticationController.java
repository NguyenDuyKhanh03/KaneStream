package com.example.KaneStream.domain.user.controller;

import com.example.KaneStream.domain.user.LoginRequest;
import com.example.KaneStream.domain.user.LoginResponse;
import com.example.KaneStream.domain.user.RegisterAccountRequest;
import com.example.KaneStream.domain.user.UserDto;
import com.example.KaneStream.domain.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
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
