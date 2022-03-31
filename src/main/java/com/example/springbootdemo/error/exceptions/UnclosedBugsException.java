package com.example.springbootdemo.error.exceptions;

public class UnclosedBugsException extends RuntimeException{
    public UnclosedBugsException(String msg){
        super(msg);
    }
}
