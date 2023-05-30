package com.dzhatdoev.vk.util;

import com.dzhatdoev.vk.util.exceptions.PersonNotCreatedException;
import com.dzhatdoev.vk.util.exceptions.PersonNotFoundException;
import com.dzhatdoev.vk.util.exceptions.PersonNotLoggedException;
import com.dzhatdoev.vk.util.exceptions.PostNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse onAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(PersonNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onPersonNotCreatedException(PersonNotCreatedException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PersonNotFoundException.class)
    private ErrorResponse onPersonNotFoundException(PersonNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    private ErrorResponse onPostNotFoundException(PostNotFoundException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PersonNotLoggedException.class)
    private ErrorResponse onPersonNotLoggedException(PersonNotLoggedException e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now());
    }


//    @ResponseBody
//    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    private PersonErrorResponse handleException(HttpMediaTypeNotSupportedException e) {
//        return new PersonErrorResponse(e.getMessage(),LocalDateTime.now());
//    }


}
