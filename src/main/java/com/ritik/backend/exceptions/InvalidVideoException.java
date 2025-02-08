package com.ritik.backend.exceptions;

public class InvalidVideoException extends RuntimeException{

    public InvalidVideoException(String message){
        super(message);
    }

}
