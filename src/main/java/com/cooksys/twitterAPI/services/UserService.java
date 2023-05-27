package com.cooksys.twitterAPI.services;

import com.cooksys.twitterAPI.dtos.CredentialsDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserRequestDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
	List<UserResponseDto> getAllUsers();

	void followUser(CredentialsDto credentialsDto, String username);

	void unfollowUser(CredentialsDto credentialsDto, String username);

	List<TweetResponseDto> getFeed(String username);

	List<TweetResponseDto> getUserMentions(String username);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	UserResponseDto getUserByUsername(String username);

	UserResponseDto createOrReactivateUser(UserRequestDto userRequestDto);

	List<UserResponseDto> getActiveFollowers(String username);

	List<UserResponseDto> getActiveFollowing(String username);
}
