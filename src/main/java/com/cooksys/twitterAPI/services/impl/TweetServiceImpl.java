package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.dtos.*;
import com.cooksys.twitterAPI.entities.Credentials;
import com.cooksys.twitterAPI.entities.Tweet;
import com.cooksys.twitterAPI.entities.User;
import com.cooksys.twitterAPI.exceptions.BadRequestException;
import com.cooksys.twitterAPI.exceptions.NotFoundException;
import com.cooksys.twitterAPI.mappers.CredentialsMapper;
import com.cooksys.twitterAPI.mappers.HashtagMapper;
import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.mappers.UserMapper;
import com.cooksys.twitterAPI.repositories.TweetRepository;
import com.cooksys.twitterAPI.repositories.UserRepository;
import com.cooksys.twitterAPI.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private UserServiceImpl userServiceImpl;
    private HashtagMapper hashtagMapper;

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

    // GET REPLIES
    @Override
    public List<TweetResponseDto> getReplies(Long id) {
        Tweet tweet = getTweetById(id);
        List<Tweet> result = new ArrayList<>();
        for (Tweet reply : tweet.getReplies()) {
            if (!reply.isDeleted()) {
                result.add(reply);
            }
        }
        return tweetMapper.entitiesToDtos(result);
    }

    // GET NEXT TWEET
    private List<Tweet> getNextTweet(Tweet reply, List<Tweet> after) {
        if (!reply.isDeleted()) {
            after.add(reply);
        }
        if (reply.getReplies() != null) {
            for (Tweet replyToReply : reply.getReplies()) {
                getNextTweet(replyToReply, after);
            }
        }
        return after;
    }

    // GET CONTEXT
    @Override
    public ContextDto getContext(Long id) {
        Tweet target = getTweetById(id);

        ContextDto context = new ContextDto();
        context.setTarget(tweetMapper.entityToDto(target));

        List<Tweet> after = new ArrayList<>();
        for (Tweet reply : target.getReplies()) {
            getNextTweet(reply, after);
        }
        context.setAfter(tweetMapper.entitiesToDtos(after));

        List<Tweet> before = new ArrayList<>();
        while (target.getInReplyTo() != null) {
            if (target.getInReplyTo().isDeleted() == false) {
                before.add(target.getInReplyTo());
            }
            target = target.getInReplyTo();
        }
        context.setBefore(tweetMapper.entitiesToDtos(before));

        return context;
    }

    // GET LIKES
    @Override
    public List<UserResponseDto> getLikes(Long id) {
        List<User> tweetLikes = getTweetById(id).getLikedByUsers();
        return userMapper.entitiesToDtos(tweetLikes);
    }

    // GET TWEET TAGS
    @Override
    public List<HashtagDto> getHashtags(Long id) {
        return hashtagMapper.entitiesToDtos(getTweetById(id).getHashtags());
    }

    //REPOST TWEET
    @Override
    public TweetResponseDto repostTweet(CredentialsDto credentialsDto, Long id) {

        Tweet target = getTweetById(id);

        User author = getUserByCredentials(credentialsMapper.dtoToEntity(credentialsDto));
        Tweet repost = new Tweet();
        repost.setAuthor(author);
        repost.setPosted(Timestamp.from(Instant.now()));
        repost.setDeleted(false);
        repost.setContent(null);
        repost.setRepostOf(target);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repost));
    }


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

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        if (tweetRequestDto.getContent() == null) {
            throw new BadRequestException("A new tweet requires content");
        }

        Credentials credentials = credentialsMapper.dtoToEntity(tweetRequestDto.getCredentials());
        Optional<User> optionalUser = userRepository.findByCredentialsAndDeletedFalse(credentials);
        if (optionalUser.isEmpty()) {
            throw new BadRequestException("the request is not from a valid user");
        }

        User validUser = optionalUser.get();
        Tweet tweetToCreate = tweetMapper.requestDtoToEntity(tweetRequestDto);
        tweetToCreate.setAuthor(validUser);

        // work on the tweet content
        // getContent()
        String content = tweetToCreate.getContent();
        // tweet with mentions needs a list of mentioned users
        if (content.contains("@")) {
            List<User> mentionedUsers = new ArrayList<>();
            // Create a pattern to match substrings between "@" symbols and spaces or end of string
            Pattern mentionedPattern = Pattern.compile("@(\\w+)(?:\\s|$)");
            Matcher matcher = mentionedPattern.matcher(content);
            while (matcher.find()) {
                // matcher.group(1) moves 1 to the right of the @ symbol, excluding it
                String usernameFromContent = matcher.group(1);
                // check if username matches existing user and get the user
                User mentionedUser = userServiceImpl.getUserEntity(usernameFromContent);
                mentionedUsers.add(mentionedUser);
            }
            // setMentionedUsers
           tweetToCreate.setMentionedUsers(mentionedUsers);
        }
//        //process content for hashtags
//        if (content.contains("#")) {
//            List<Hashtag> mentionedHashtags = new ArrayList<>();
//            // Create a pattern to match substrings between "#" symbols and spaces or end of string
//            Pattern hashtagPattern = Pattern.compile("#(\\w+)(?:\\s|$)");
//            Matcher matcher = hashtagPattern.matcher(content);
//            while (matcher.find()) {
//                String hashtagLabelFromContent = matcher.group(0);
//                Hashtag hashtagToAdd = new Hashtag();
//                hashtagToAdd.setLabel(hashtagLabelFromContent);
//                mentionedHashtags.add(hashtagToAdd);
//            }
//            //set tweet's hashtags
//            tweetToCreate.setHashtags(mentionedHashtags);
//        }

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
    }

    // Creates a "like" relationship between the tweet with the given id and the user whose credentials are provided by the request body.
    // If the tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database,
    // an error should be sent. Following successful completion of the operation, no response body is sent.
    // POST - CREATE A TWEET LIKE
    @Override
    public void createATweetLike(Long id, CredentialsDto credentialsDto) {
        // check if tweet exists and is not deleted
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Tweet not found"));
        // check if user credentials exist and user is not deleted
        Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
        User user = userRepository.findByCredentialsAndDeletedFalse(credentials)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.getLikedTweets().add(tweet);
        userRepository.saveAndFlush(user);
        tweet.getLikedByUsers().add(user);
        tweetRepository.saveAndFlush(tweet);
    }

    // DELETE - TWEET BY ID
    @Override
    public TweetResponseDto softDeleteTweet(Long id, CredentialsDto credentialsDto) {
        // check if tweet exists and is not deleted
        Tweet tweetToDelete = tweetRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Tweet not found"));
        // check if user credentials exist and user is not deleted
        Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
        User user = userRepository.findByCredentialsAndDeletedFalse(credentials)
                .orElseThrow(() -> new NotFoundException("User not found"));

        tweetToDelete.setDeleted(true);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToDelete));
    }

}
