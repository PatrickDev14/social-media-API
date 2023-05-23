package com.cooksys.twitterAPI.exceptions;

import java.io.Serial;

public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4164372187627400280L;

    private String message;
}
