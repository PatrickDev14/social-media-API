package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.dtos.TweetRequestDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.entities.Tweet;
import com.cooksys.twitterAPI.exceptions.BadRequestException;
import com.cooksys.twitterAPI.exceptions.NotFoundException;
import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.repositories.TweetRepository;
import com.cooksys.twitterAPI.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    @Override
    public List<TweetResponseDto> getAllSavedTweets() {
        List<Tweet> foundTweets = tweetRepository.findAll();
        foundTweets.removeIf(Tweet::isDeleted);
        foundTweets.sort(Comparator.comparing(Tweet::getPosted, Comparator.reverseOrder()));

        return tweetMapper.entitiesToDtos(foundTweets);
    }

    /* GET tweets/{id}
     *  Retrieves a tweet with a given id. If no such tweet exists, or the given tweet is deleted, an error should be sent
     * in lieu of a response.
     */

    @Override
    public TweetResponseDto getTweet(Long id) {
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty() || potentialTweet.get().isDeleted()) {
            throw new NotFoundException("Tweet was not found");
        }

        return tweetMapper.entityToDto(potentialTweet.get());
    }

    /* POST tweets
     * Creates a new simple tweet, with the author set to the user identified by the credentials in the request body.
     * If the given credentials do not match an active user in the database, an error should be sent in lieu of a response.
     * The response should contain the newly-created tweet.
     * Because this always creates a simple tweet, it must have a content property and may not have inReplyTo or repostOf properties.
     * IMPORTANT: when a tweet with content is created, the server must process the tweet's content for @{username} mentions
     * and #{hashtag} tags. There is no way to create hashtags or create mentions from the API, so this must be handled automatically!
     */
    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        String username = tweetRequestDto.getCredentials().getUsername();
        //  Optional<Credential> to make sure they're not null
//        if(!validateService.usernameExists(username)
//          && tweetRequestDto.getCredentials().getPassword == ) {
//            throw new NotFoundException("username was not found.");
//        }

        Tweet tweetToCreate = tweetMapper.requestDtoToEntity(tweetRequestDto);
//        tweetToCreate.setAuthor(tweetRequestDto.getCredentials().getUsername());
        if (tweetToCreate.getContent().isEmpty()) {
            throw new BadRequestException("A new tweet requires content");
        }

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
    }


    // Creates a "like" relationship between the tweet with the given id and the user whose credentials are provided by the request body.
    // If the tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database,
    // an error should be sent. Following successful completion of the operation, no response body is sent.

}
