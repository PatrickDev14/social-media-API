package com.cooksys.twitterAPI.services;

import com.cooksys.twitterAPI.dtos.*;

import java.util.List;

public interface TweetService {
	TweetResponseDto replyToTweet(TweetRequestDto tweetRequestDto, Long id);

	List<TweetResponseDto> getReposts(Long id);

	List<UserResponseDto> getMentions(Long id);

	List<TweetResponseDto> getReplies(Long id);

	TweetResponseDto repostTweet(CredentialsDto credentialsDto, Long id);

	ContextDto getContext(Long id);

	List<UserResponseDto> getLikes(Long id);

	List<HashtagDto> getHashtags(Long id);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto getTweet(Long id);

	List<TweetResponseDto> getAllSavedTweets();
}
