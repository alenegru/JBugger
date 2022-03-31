package com.example.springbootdemo.error.exceptions;

public class UserInactiveException extends RuntimeException{
    public UserInactiveException(String msg){
        super(msg);
    }
}

