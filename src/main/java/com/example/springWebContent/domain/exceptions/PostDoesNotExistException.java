package com.example.springWebContent.domain.exceptions;

public class PostDoesNotExistException extends Exception{
    public PostDoesNotExistException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
