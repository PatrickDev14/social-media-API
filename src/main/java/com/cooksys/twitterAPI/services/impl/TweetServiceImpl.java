package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.dtos.TweetRequestDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;
import com.cooksys.twitterAPI.entities.Credentials;
import com.cooksys.twitterAPI.entities.Tweet;
import com.cooksys.twitterAPI.entities.User;
import com.cooksys.twitterAPI.exceptions.BadRequestException;
import com.cooksys.twitterAPI.exceptions.NotFoundException;
import com.cooksys.twitterAPI.mappers.CredentialsMapper;
import com.cooksys.twitterAPI.mappers.UserMapper;
import com.cooksys.twitterAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.repositories.TweetRepository;
import com.cooksys.twitterAPI.services.TweetService;

import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TweetMapper tweetMapper;
    @Autowired
    private CredentialsMapper credentialsMapper;
    @Autowired
    private UserMapper userMapper;

    //HELPER METHOD TO GET USER MY CREDENTIALS
    private User getUserByCredentials(Credentials credentials) {
        Optional<User> optionalUser = userRepository.findByCredentialsAndDeletedFalse(credentials);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No user found with credentials: ");// + credentials.toString());
        } else if (optionalUser.get().isDeleted()) {
            throw new BadRequestException("This user is inactive.");
        }
        return optionalUser.get();
    }

    //HELPER METHOD TO VALIDATE A TWEET REQUEST FOR REPLY METHOD BELOW
    private void validateTweetRequest(TweetRequestDto tweetRequestDto) {
        if (tweetRequestDto.getContent() == null) {
            throw new BadRequestException("Missing content on tweet request dto");
        } else if (tweetRequestDto.getCredentials().getUsername() == null
                || tweetRequestDto.getCredentials().getPassword() == null) {
            throw new BadRequestException("Missing credentials on tweet request dto");
        }
    }

    //HELPER METHOD TO FIND BY ID
    private Tweet getTweetById(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (optionalTweet.isEmpty()) {
            throw new NotFoundException("No tweet found with id: " + id);
        } else if (optionalTweet.get().isDeleted()) {
            throw new BadRequestException("This tweet was deleted");
        }
        return optionalTweet.get();
    }

    //POST - REPLY TO TWEET
    @Override
    public TweetResponseDto replyToTweet(TweetRequestDto tweetRequestDto, Long id) {
        Tweet rootTweet = getTweetById(id);
        Tweet replyingTweet = tweetMapper.dtoToEntity(tweetRequestDto);
        User author = getUserByCredentials(credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials()));
        validateTweetRequest(tweetRequestDto);
        replyingTweet.setInReplyTo(rootTweet);
        replyingTweet.setAuthor(author);
        replyingTweet.setPosted(Timestamp.from(Instant.now()));
        rootTweet.getReplies().add(replyingTweet);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(replyingTweet));
    }

    //GET - REPOSTS BY ID
    @Override
    public List<TweetResponseDto> getReposts(Long id) {
        Tweet rootTweet = getTweetById(id);
        List<Tweet> repost = new ArrayList<>();
        for (Tweet tweet : rootTweet.getReposts()) {
            if (!tweet.isDeleted()) {
                repost.add(tweet);
            }
        }
        return tweetMapper.entitiesToDtos(repost);
    }

    //GET - MENTIONS BY ID
    @Override
    public List<UserResponseDto> getMentions(Long id) {
        Tweet tweet = getTweetById(id);
        List<UserResponseDto> allMentions = new ArrayList<>();
        for (User mentioned : tweet.getMentionedUsers()) {
            if (!mentioned.isDeleted()) {
                allMentions.add(userMapper.entityToDto(mentioned));
            }
        }
        return allMentions;
    }
}
