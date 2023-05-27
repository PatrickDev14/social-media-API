package com.cooksys.twitterAPI.services;

import com.cooksys.twitterAPI.dtos.ContextDto;
import com.cooksys.twitterAPI.dtos.HashtagDto;
import com.cooksys.twitterAPI.dtos.TweetRequestDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;

import java.util.List;

public interface TweetService {
	TweetResponseDto replyToTweet(TweetRequestDto tweetRequestDto, Long id);

	List<TweetResponseDto> getReposts(Long id);

	List<UserResponseDto> getMentions(Long id);

	List<TweetResponseDto> getReplies(Long id);

	ContextDto getContext(Long id);

	List<UserResponseDto> getLikes(Long id);

	List<HashtagDto> getTweetTags(Long id);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto getTweet(Long id);

	List<TweetResponseDto> getAllSavedTweets();
}
