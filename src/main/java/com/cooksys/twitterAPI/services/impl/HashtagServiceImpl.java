package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.dtos.HashtagDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.entities.Hashtag;
import com.cooksys.twitterAPI.exceptions.NotFoundException;
import com.cooksys.twitterAPI.mappers.HashtagMapper;
import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.repositories.HashtagRepository;
import com.cooksys.twitterAPI.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	@Autowired
	private final HashtagRepository hashtagRepository;
	@Autowired
	private final TweetMapper tweetMapper;
	@Autowired
	private final HashtagMapper hashtagMapper;

	// GET - ALL TWEETS BY HASHTAG
	@Override
	public List<TweetResponseDto> getAllTweetsByHashtag(String label) {
		Optional<Hashtag> hashtagByLabel = hashtagRepository.findHashtagByLabel(label);

		if (hashtagByLabel.isPresent()) {
			Hashtag hashtag = hashtagByLabel.get();
			return tweetMapper.entitiesToDtos(hashtag.getTweets());
		} else {
			throw new NotFoundException("label does not exist");
//			return Collections.emptyList();
		}
	}

	// GET ALL HASHTAGS
	@Override
	public List<HashtagDto> getAllHashtags() {
		List<Hashtag> hashtags = hashtagRepository.findAll();
		return hashtagMapper.entitiesToDtos(hashtags);
	}
}
