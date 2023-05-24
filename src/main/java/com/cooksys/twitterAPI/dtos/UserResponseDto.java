package com.cooksys.twitterAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Profile;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDto {

    private String username;

    private Profile profile;

    @CreationTimestamp
    private Date joined;

}
