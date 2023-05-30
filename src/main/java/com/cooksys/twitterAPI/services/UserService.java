package com.cooksys.twitterAPI.services;

import com.cooksys.twitterAPI.dtos.CredentialsDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserRequestDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;
import com.cooksys.twitterAPI.exceptions.BadRequestException;
import com.cooksys.twitterAPI.exceptions.NotAuthorizedException;

import java.util.List;

public interface UserService {
	List<UserResponseDto> getAllUsers();

	void followUser(CredentialsDto credentialsDto, String username) throws NotAuthorizedException, BadRequestException;

	void unfollowUser(CredentialsDto credentialsDto, String username);

	List<TweetResponseDto> getFeed(String username);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	List<UserResponseDto> getActiveFollowing(String username);

	List<TweetResponseDto> getTweetsMentioningUsername(String username);

	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	UserResponseDto getUserByUsername(String username);

	UserResponseDto createOrReactivateUser(UserRequestDto userRequestDto);

	List<UserResponseDto> getActiveFollowers(String username);

    List<TweetResponseDto> getTweets(String username);
}
