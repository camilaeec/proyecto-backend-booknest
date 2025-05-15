package com.example.booknest;

import com.example.booknest.exception.PasswordIncorrectException;
import com.example.booknest.exception.ResourceAlreadyExists;
import com.example.booknest.exception.ResourceNotFoundException;
import com.example.booknest.exception.UserMustBeAuthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserMustBeAuthenticatedException.class)
    public String handleUserMustBeAuthenticatedException(UserMustBeAuthenticatedException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExists.class)
    public String handleResourceAlreadyExistsException(ResourceAlreadyExists ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(PasswordIncorrectException.class)
    public String handlePasswordIncorrectException(PasswordIncorrectException ex){
        return ex.getMessage();
    }
}
