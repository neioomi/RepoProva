package com.example.automobili.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
//per specificare il codice di stato della risposta HTTP che deve essere restituito quando viene lanciata un'eccezione
public class MileageDecrementException extends RuntimeException {
    public MileageDecrementException(String message) {

        super(message);
    }
}
