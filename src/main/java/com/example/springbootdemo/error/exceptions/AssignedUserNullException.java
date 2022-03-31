package com.example.springbootdemo.error.exceptions;

public class AssignedUserNullException extends  RuntimeException{
    public AssignedUserNullException(String msg){
        super(msg);
    }
}
