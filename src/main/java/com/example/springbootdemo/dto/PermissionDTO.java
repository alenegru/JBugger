package com.example.springbootdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionDTO {
    private Long idPermission;
    private String type;
    private String description;
}
