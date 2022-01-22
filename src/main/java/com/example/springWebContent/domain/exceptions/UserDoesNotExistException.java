package com.example.springWebContent.domain.exceptions;

public class UserDoesNotExistException extends Exception{
    public UserDoesNotExistException(String message){
        super(message);
    }

    public UserDoesNotExistException(){
        super("user does not exist in database");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
