package com.cooksys.twitterAPI.controllers;

import com.cooksys.twitterAPI.dtos.HashtagDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.services.HashtagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class HashtagController {

    private final HashtagService hashtagService;

    //GET - TWEETS BY HASHTAG
    @GetMapping("/{label}")
    public List<TweetResponseDto> getAllTweetsByHashtag(@PathVariable String label) {
        return hashtagService.getAllTweetsByHashtag(label);
    }

    @GetMapping
    public List<HashtagDto> getAllHashtags() {
        return hashtagService.getAllHashtags();
    }
}
