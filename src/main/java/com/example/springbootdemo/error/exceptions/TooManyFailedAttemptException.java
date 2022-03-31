package com.example.springbootdemo.error.exceptions;

public class TooManyFailedAttemptException extends RuntimeException{
    public TooManyFailedAttemptException(String msg){
        super(msg);
    }
}
