package com.cooksys.twitterAPI.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {

    private Integer id;

    private UserResponseDto author;

    private Timestamp posted;

    private String content;
    private TweetResponseDto inReplyTo;
    private TweetResponseDto repostOf;

}
