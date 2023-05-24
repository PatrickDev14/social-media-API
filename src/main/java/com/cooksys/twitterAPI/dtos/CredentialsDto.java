package com.cooksys.twitterAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CredentialsDto {

    private String username;

    private String password;

}
