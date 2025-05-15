package com.example.booknest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserMustBeAuthenticatedException extends RuntimeException{
    public UserMustBeAuthenticatedException(String message){
        super(message);
    }
}
