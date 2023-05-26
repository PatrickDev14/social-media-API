package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.dtos.CredentialsDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;
import com.cooksys.twitterAPI.entities.Tweet;
import com.cooksys.twitterAPI.entities.User;
import com.cooksys.twitterAPI.exceptions.BadRequestException;
import com.cooksys.twitterAPI.exceptions.NotAuthorizedException;
import com.cooksys.twitterAPI.exceptions.NotFoundException;
import com.cooksys.twitterAPI.mappers.CredentialsMapper;
import com.cooksys.twitterAPI.mappers.TweetMapper;
import com.cooksys.twitterAPI.mappers.UserMapper;
import com.cooksys.twitterAPI.repositories.TweetRepository;
import com.cooksys.twitterAPI.repositories.UserRepository;
import com.cooksys.twitterAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final CredentialsMapper credentialsMapper;
    @Autowired
    private final TweetMapper tweetMapper;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TweetRepository tweetRepository;
    @Autowired
    private final ValidateServiceImpl validateServiceImpl;

    //HELPER METHOD TO CHECK IF USER EXISTS
    private User getUserEntity(String username) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        if (!validateServiceImpl.usernameExists(username)
                || userRepository.findByCredentialsUsernameAndDeletedFalse(username).isEmpty()
                || userRepository.findByCredentialsUsernameAndDeletedFalse(username).get().isDeleted()) {
            throw new NotFoundException("User not found with username: " + username);
        }
        return optionalUser.get();
    }


    //GET - ALL USERS
    @Override
    public List<UserResponseDto> getAllUsers() {

        List<User> userList = userRepository.findAllByDeletedFalse();
        List<UserResponseDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserResponseDto userDto = userMapper.entityToDto(user);
            userDto.setUsername(user.getCredentials().getUsername());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    //POST - FOLLOW USER
    @Override
    public void followUser(CredentialsDto credentialsDto, String username)
            throws NotAuthorizedException, BadRequestException {
        boolean checker = false;
        for (User userCheck : userRepository.findAll()) {
            if (credentialsMapper.dtoToEntity(credentialsDto).equals(userCheck.getCredentials())) {
                User followerCheck = getUserEntity(credentialsDto.getUsername());
                if (getUserEntity(username).getFollowers().contains(followerCheck)) {

                    throw new BadRequestException("user is already followed by this person");
                }
                User follower = getUserEntity(credentialsMapper.dtoToEntity(credentialsDto).getUsername());
                User followed = getUserEntity(username);
                List<User> followingList = follower.getFollowing();
                followingList.add(followed);
                follower.setFollowing(followingList);
                userRepository.saveAndFlush(follower);
                checker = true;
            }
        }
        if (!checker) {
            throw new NotAuthorizedException("Credentials do not match");
        }
    }

    //POST - UNFOLLOW USER
    @Override
    public void unfollowUser(CredentialsDto credentialsDto, String username)
            throws NotAuthorizedException, BadRequestException {
        boolean checker = false;
        for (User userCheck : userRepository.findAll()) {
            if (userCheck.getCredentials().equals(credentialsMapper.dtoToEntity(credentialsDto))) {
                User followerCheck = getUserEntity(credentialsDto.getUsername());
                if (!getUserEntity(username).getFollowers().contains(followerCheck)) {
                    throw new BadRequestException("user is not followed by this person");
                } else {
                    User unfollower = getUserEntity(credentialsDto.getUsername());
                    User unfollowed = getUserEntity(username);
                    List<User> following = unfollower.getFollowing();
                    following.remove(unfollowed);
                    unfollower.setFollowing(following);
                    userRepository.saveAndFlush(unfollower);
                    checker = true;
                }
            }
        }
        if (!checker) {
            throw new NotAuthorizedException("Credentials do not match");
        }

    }

    //GET - USER FEED
    @Override
    public List<TweetResponseDto> getFeed(String username) {

        User user = getUserEntity(username);

        List<TweetResponseDto> feed = new ArrayList<>();
        for (Tweet tweet : tweetRepository.findAll()) {
            if (user.getFollowing().contains(tweet.getAuthor())
                    || user.getTweets().contains(tweet) && !tweet.isDeleted()) {
                feed.add(tweetMapper.entityToDto(tweet));
            }
        }
        List<TweetResponseDto> feedReversed = new ArrayList<>();
        for (int i = feed.size() - 1; i >= 0; i--) {
            feedReversed.add(feed.get(i));
        }
        return feedReversed;
    }

}
