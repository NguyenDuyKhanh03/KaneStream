package com.example.KaneStream.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String avatar;
    private String username;
    private String bio;
    private int posts;
    private int followers;
    private int following;
    private String portfolio;

}
