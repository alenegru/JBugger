package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.CommentDTO;
import com.example.springbootdemo.model.Comment;
import com.example.springbootdemo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/comments/")
    public List<CommentDTO> findAll() {
        return commentService.findAll();
    }

    @PostMapping("/comments/add")
    public Comment addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @DeleteMapping("/comments/delete/{id}")
    public Boolean deleteCommentsById(@PathVariable Long id) {
        return commentService.removeComment(id);
    }

    @GetMapping("/comments/{id}")
    public CommentDTO findById(@PathVariable Long id) {
        return commentService.findCommentById(id);
    }
}
