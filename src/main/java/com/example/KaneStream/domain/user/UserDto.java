package com.example.KaneStream.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String avatar;
    private String username;
}
