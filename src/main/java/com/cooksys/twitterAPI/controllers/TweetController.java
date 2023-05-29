package com.cooksys.twitterAPI.controllers;

import com.cooksys.twitterAPI.dtos.*;
import com.cooksys.twitterAPI.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<TweetResponseDto> getAllSavedTweets() {
        return tweetService.getAllSavedTweets();
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweet(@PathVariable Long id) {
        return tweetService.getTweet(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.createTweet(tweetRequestDto);
    }

    // POST - CREATE A TWEET LIKE
    @PostMapping("/{id}/like")
    public void createATweetLike(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        tweetService.createATweetLike(id, credentialsDto);
    }

    // DELETE - TWEET BY ID
    @DeleteMapping("/{id}")
    public TweetResponseDto softDeleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.softDeleteTweet(id, credentialsDto);
    }
  
    //GET TWEET LIKES
    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getLikes(@PathVariable Long id) {
        return tweetService.getLikes(id);
    }

    //GET TWEETS BY TAGS
    @GetMapping("/{id}/tags")
    public List<HashtagDto> getHashtags(@PathVariable Long id) {
        return tweetService.getHashtags(id);
    }

    //GET CONTEXT
    @GetMapping("/{id}/context")
    public ContextDto getContext(@PathVariable Long id) {
        return tweetService.getContext(id);
    }

    //POST REPOST
    @PostMapping("/{id}/repost")
    public TweetResponseDto repostTweet(@RequestBody CredentialsDto credentialsDto, @PathVariable Long id) {
        return tweetService.repostTweet(credentialsDto, id);
    }

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getReplies(@PathVariable Long id) {
        return tweetService.getReplies(id);
    }
}
