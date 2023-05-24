package com.cooksys.twitterAPI.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.repositories.TweetRepository;
import com.cooksys.twitterAPI.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private TweetRepository tweetRepository;
    private TweetMapper tweetMapper;

}
