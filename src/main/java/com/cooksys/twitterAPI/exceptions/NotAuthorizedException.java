package com.cooksys.twitterAPI.exceptions;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -875567105863638012L;

    private String message;
}
