package com.cooksys.twitterAPI.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitterAPI.dtos.HashtagDto;
import com.cooksys.twitterAPI.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
    List<HashtagDto> entitiesToDtos(List<Hashtag> dtos);

    HashtagDto entityToDto(Hashtag hashtag);

    Hashtag dtoToEntity(HashtagDto dto);
}
