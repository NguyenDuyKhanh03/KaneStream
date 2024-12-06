package com.example.KaneStream.mapper.impl;

import com.example.KaneStream.domain.user.User;
import com.example.KaneStream.domain.user.UserDto;
import com.example.KaneStream.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements Mapper<User, UserDto> {
    private final ModelMapper modelMapper;
    @Override
    public UserDto mapFrom(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User mapTo(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
