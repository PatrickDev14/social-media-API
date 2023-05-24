package com.cooksys.twitterAPI.dtos;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TweetResponseDto {

    private Integer id;

    private UserRequestDto author;

    private Timestamp posted;

    private String content;

    @Nullable
    private TweetRequestDto inReplyOf;
    @Nullable
    private TweetRequestDto repostOf;

}
