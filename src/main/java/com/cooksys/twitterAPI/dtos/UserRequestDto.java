package com.cooksys.twitterAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDto {

    private CredentialsDto credentials;

    private ProfileDto profile;

}
