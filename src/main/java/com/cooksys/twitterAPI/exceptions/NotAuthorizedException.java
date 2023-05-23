package com.cooksys.twitterAPI.exceptions;

import java.io.Serial;

public class NotAuthorizedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -875567105863638012L;

    private String message;
}
