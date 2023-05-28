package com.cooksys.twitterAPI.controllers;

import com.cooksys.twitterAPI.dtos.CredentialsDto;
import com.cooksys.twitterAPI.dtos.TweetResponseDto;
import com.cooksys.twitterAPI.dtos.UserRequestDto;
import com.cooksys.twitterAPI.dtos.UserResponseDto;
import com.cooksys.twitterAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    //GET - ALL USERS
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    //POST - FOLLOW USER
    @PostMapping("/@{username}/follow")
    public void followUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username) {
        userService.followUser(credentialsDto, username);
    }

    //POST - UNFOLLOW USER
    @PostMapping("/@{username}/unfollow")
    public void unfollowUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username) {
        userService.unfollowUser(credentialsDto, username);
    }

    //GET - USER FEED
    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getFeed(@PathVariable String username) {
        return userService.getFeed(username);
    }

    @PostMapping
    public UserResponseDto createOrReactivateUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createOrReactivateUser(userRequestDto);
    }

    // GET - FOLLOWERS BY USERNAME
    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getActiveFollowers(@PathVariable String username) {
        return userService.getActiveFollowers(username);
    }

    // GET - FOLLOWING BY USERNAME
    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getActiveFollowing(@PathVariable String username) {
        return userService.getActiveFollowing(username);
    }

    // GET - TWEETS THAT MENTION USERNAME
    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getTweetsMentioningUsername(@PathVariable String username) {
        return userService.getTweetsMentioningUsername(username);
    }

//    // GET USER MENTIONS
//    @GetMapping("/@{username}/mentions")
//    public List<TweetResponseDto> getMentions(@PathVariable String username) {
//        return userService.getUserMentions(username);
//    }

    // DELETE A USER
    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username) {
        return userService.deleteUser(username, credentialsDto);
    }

    //UPDATE USER
    @PatchMapping("/@{username}")
    public UserResponseDto updateUser(@RequestBody UserRequestDto userDto, @PathVariable String username) {
        return userService.updateUser(username, userDto);
    }

    //GET USER BY USERNAME
    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

}
