package com.example.springbootdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
public class CommentDTO {
    private String text;
    private Timestamp date;
}
