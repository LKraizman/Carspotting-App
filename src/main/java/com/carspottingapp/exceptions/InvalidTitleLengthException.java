package com.carspottingapp.exceptions;

public class InvalidTitleLengthException extends Exception{
    public InvalidTitleLengthException(String description){
        super(description);
    }
}
