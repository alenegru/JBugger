package com.example.springbootdemo.model;

import com.example.springbootdemo.dto.PermissionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissions_gen")
    @SequenceGenerator(name = "permissions_gen", sequenceName = "permissions_seq", allocationSize = 1)
    private Long idPermission;
    @Column(unique = true)
    private String type;
    private String description;

    public static Permission mapToPermission(PermissionDTO dto){
        Permission permission=new Permission();
        permission.setDescription(dto.getDescription());
        permission.setType(dto.getType());
        permission.setIdPermission(dto.getIdPermission());
        return permission;
    }
    public static PermissionDTO mapToDTO(Permission permission){
        PermissionDTO dto=new PermissionDTO();
        dto.setDescription(permission.getDescription());
        dto.setType(permission.getType());
        dto.setIdPermission(permission.getIdPermission());
        return dto;
    }


}
