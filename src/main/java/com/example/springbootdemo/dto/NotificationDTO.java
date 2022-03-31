package com.example.springbootdemo.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String Type;
    private String url;
    private String message;
    private Boolean read;
}
