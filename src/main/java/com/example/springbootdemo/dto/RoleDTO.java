package com.example.springbootdemo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoleDTO {
    private Long idRole;
    private String type;
    private List<PermissionDTO> permissions;
}
