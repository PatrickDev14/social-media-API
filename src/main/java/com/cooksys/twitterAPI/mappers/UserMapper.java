package com.cooksys.twitterAPI.mappers;

import com.cooksys.twitterAPI.dtos.UserRequestDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;
import com.cooksys.twitterAPI.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {
    @Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User user);

    User responseDtoToEntity(UserResponseDto userResponseDto);

    User requestDtoToEntity(UserRequestDto userRequestDto);

    List<UserResponseDto> entitiesToDtos(List<User> users);
}
