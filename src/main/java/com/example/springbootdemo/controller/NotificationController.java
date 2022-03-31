package com.example.springbootdemo.controller;


import com.example.springbootdemo.dto.NotificationDTO;
import com.example.springbootdemo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", originPatterns = "*", allowedHeaders = "*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NotificationDTO>> findByUsername(@PathVariable String username) {
        return notificationService.findNotificationsByUser(username);
    }

    @GetMapping("/notification/readNotificationCheck/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> checkIfRead(@PathVariable String username) {
        return notificationService.checkIfRead(username);
    }

    @PutMapping("/notification/status/{id}")
    public Boolean setStatusToFalse(@PathVariable Long id) {
        return notificationService.changeStatusNotification(id);
    }

}
