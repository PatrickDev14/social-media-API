package com.cooksys.twitterAPI.mappers;

import org.mapstruct.Mapper;

import com.cooksys.twitterAPI.dtos.CredentialsDto;
import com.cooksys.twitterAPI.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	
	CredentialsDto entityToDto(Credentials credentials);

    Credentials dtoToEntity(CredentialsDto credentials);
}
