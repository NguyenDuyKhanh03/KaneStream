package com.example.KaneStream.domain.user.service;

import com.example.KaneStream.domain.user.*;
import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.exeption.ResourceAlreadyExistsException;
import com.example.KaneStream.exeption.ResourceNotFoundException;
import com.example.KaneStream.mapper.Mapper;
import com.example.KaneStream.util.JWTService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    private final Mapper<User, UserDto> mapper;

    private final JWTService jwtService;

    private final UserService userService;

    @Transactional
    public UserDto register(RegisterAccountRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if(!Objects.isNull(user)) {
            throw new ResourceAlreadyExistsException("User already exists");
        }
        user=new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhoneNumber());
        user.setStatus(UserStatus.active);
        user.setSalt(generateSalt());

        System.out.println("Status: "+user.getStatus());
        user.setRole(Role.user);

        System.out.println("Role: " + user.getRole());
        userRepository.save(user);
        return mapper.mapFrom(user);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(authentication.isAuthenticated()) {
            return new LoginResponse(jwtService.generateToken(request.getUsername()));
        }
        else{
            throw new ResourceNotFoundException("Authentication failed");
        }
    }



    private String generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
