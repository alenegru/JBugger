package com.example.springbootdemo.error.exceptions;

public class UserPasswordIncorrectException extends RuntimeException {
    public UserPasswordIncorrectException(String s) {
        super(s);
    }
}
