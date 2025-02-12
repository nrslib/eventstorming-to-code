package org.example.eventstormingsamplecodes.http.controllers;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
