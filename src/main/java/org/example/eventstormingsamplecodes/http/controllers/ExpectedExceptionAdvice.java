package org.example.eventstormingsamplecodes.http.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpectedExceptionAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(NotFoundException notFoundException) {
        var message = notFoundException.getMessage();
        var response = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), message);

        return response;
    }
}
