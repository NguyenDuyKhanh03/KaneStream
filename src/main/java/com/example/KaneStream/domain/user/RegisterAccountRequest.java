package com.example.KaneStream.domain.user;

import lombok.Data;

@Data
public class RegisterAccountRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
