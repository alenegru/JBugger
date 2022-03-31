package com.example.springbootdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
public class AttachmentDTO {
    private Long id;
    private byte[] content;
    private String title;
}
