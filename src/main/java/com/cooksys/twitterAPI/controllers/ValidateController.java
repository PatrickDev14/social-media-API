package com.cooksys.twitterAPI.controllers;

import com.cooksys.twitterAPI.services.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

    @Autowired
    private ValidateService validateService;

    //VALIDATE IF USERNAME IS AVAILABLE
    @GetMapping("/username/available/@{username}")
    public Boolean usernameAvailable(@PathVariable String username) {
        return validateService.usernameAvailable(username);
    }

    // VALIDATE IF HASHTAG EXISTS
    @GetMapping("/tag/exists/{label}")
    public Boolean hashtagExists(@PathVariable String label) {
        return validateService.hashtagExists(label);
    }
}
