package com.cooksys.twitterAPI.exceptions;

import java.io.Serial;

public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6996625711144625103L;

    private String message;
}
