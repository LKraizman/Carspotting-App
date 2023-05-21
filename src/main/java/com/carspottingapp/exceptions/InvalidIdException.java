package com.carspottingapp.exceptions;

public class InvalidIdException extends RuntimeException{
    public InvalidIdException(String description){
        super(description);
    }
}
