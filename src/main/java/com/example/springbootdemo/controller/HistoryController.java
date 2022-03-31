package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.BugDTO;
import com.example.springbootdemo.dto.HistoryDTO;
import com.example.springbootdemo.service.AttachmentService;
import com.example.springbootdemo.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", originPatterns = "*", allowedHeaders = "*")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @GetMapping("/history/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<HistoryDTO>> findAllByBug(@PathVariable("id") Long id) {
        return new ResponseEntity<>(historyService.findAllByBug(id), HttpStatus.OK);
    }

}
