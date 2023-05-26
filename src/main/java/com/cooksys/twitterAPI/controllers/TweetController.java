package com.cooksys.twitterAPI.controllers;

import com.cooksys.twitterAPI.dtos.TweetRequestDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;
import com.cooksys.twitterAPI.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

    @Autowired
    private final TweetService tweetService;

    //POST - REPLY TO TWEET
    @PostMapping("/{id}/reply")
    public TweetResponseDto replyToTweet(@RequestBody TweetRequestDto tweetRequestDto,
                                         @PathVariable Long id) {
        return tweetService.replyToTweet(tweetRequestDto, id);
    }

    //GET - REPOSTS BY ID
    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getReposts(@PathVariable Long id) {
        return tweetService.getReposts(id);
    }

    //GET - MENTIONS BY ID
    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getMentions(@PathVariable Long id) {
        return tweetService.getMentions(id);
    }
}
