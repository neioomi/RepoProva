package com.example.automobili.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlateNotFoundException extends Exception{

    public PlateNotFoundException() {

    }

    public PlateNotFoundException(String message) {
        super(message);
    }
}
