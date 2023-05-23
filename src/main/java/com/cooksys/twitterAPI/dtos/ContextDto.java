package com.cooksys.twitterAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContextDto {

    private TweetRequestDto target;

    private TweetRequestDto before;

    private List<TweetRequestDto> after;

}
