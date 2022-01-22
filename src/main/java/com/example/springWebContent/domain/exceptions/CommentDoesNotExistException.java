package com.example.springWebContent.domain.exceptions;

public class CommentDoesNotExistException extends Exception{
    public CommentDoesNotExistException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
