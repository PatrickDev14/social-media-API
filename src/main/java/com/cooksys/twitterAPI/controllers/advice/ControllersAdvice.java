package com.cooksys.twitterAPI.controllers.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages =  { "com.cooksys.twitterAPI.controllers" })
@ResponseBody
public class ControllersAdvice {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(BadRequestException.class)
//    public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException) {
//        return new ErrorDto(badRequestException.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NotFoundException.class)
//    public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException notFoundException) {
//        return new ErrorDto(notFoundException.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(NotAuthorizedException.class)
//    public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotAuthorizedException notAuthorizedException) {
//        return new ErrorDto(notAuthorizedException.getMessage());
//    }

}
