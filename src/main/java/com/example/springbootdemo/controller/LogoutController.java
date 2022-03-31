package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    @Autowired
    private UserService userService;


}
