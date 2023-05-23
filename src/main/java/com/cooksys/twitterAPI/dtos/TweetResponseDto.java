package com.cooksys.twitterAPI.dtos;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TweetResponseDto {

    private Integer id;

    private UserRequestDto author;

    @CreationTimestamp
    private Date posted;

    private String content;

    @Nullable
    private TweetRequestDto inReplyOf;
    @Nullable
    private TweetRequestDto repostOf;

}
