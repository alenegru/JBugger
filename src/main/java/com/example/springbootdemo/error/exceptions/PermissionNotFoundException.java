package com.example.springbootdemo.error.exceptions;

public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(String s) {
        super(s);
    }
}
