package com.cooksys.twitterAPI.services.impl;

import com.cooksys.twitterAPI.entities.User;
import com.cooksys.twitterAPI.repositories.HashtagRepository;
import com.cooksys.twitterAPI.repositories.UserRepository;
import com.cooksys.twitterAPI.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashtagRepository hashtagRepository;

    //CHECK IF USERNAME EXISTS
    @Override
    public Boolean usernameExists(String username) {
        Optional<User> findByUsername = userRepository.findByCredentialsUsername(username);
        return findByUsername.isPresent();
    }


    //VALIDATE IF USERNAME IS AVAILABLE
    @Override
    public Boolean usernameAvailable(String username) {
        Optional<User> findByUsername = userRepository.findByCredentialsUsername(username);
        if(findByUsername.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    // VALIDATE IF HASHTAG EXISTS
    @Override
    public Boolean hashtagExists(String label) {
        return hashtagRepository.findHashtagByLabel(label).isPresent();
    }
}
