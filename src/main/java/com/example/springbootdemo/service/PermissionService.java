package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.PermissionDTO;
import com.example.springbootdemo.model.Permission;
import com.example.springbootdemo.model.Role;
import com.example.springbootdemo.model.User;
import com.example.springbootdemo.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;


    public List<PermissionDTO> findAll() {
        List<Permission> permissions = (List<Permission>) permissionRepository.findAll();
        return permissions.stream().map(Permission::mapToDTO).toList();
    }

    @Transactional
    public PermissionDTO addPermission(PermissionDTO permissionDTO) {
        if(permissionDTO == null){
            throw new NullPointerException("Permission DTO was null!");
        }
        Permission permission = Permission.mapToPermission(permissionDTO);
        if (!permission.getDescription().equals("") && !permission.getType().equals(""))
            permissionRepository.save(permission);
        return Permission.mapToDTO(permission);
    }

    @Transactional
    public Boolean removePermission(Long id) {
        if (findPermissionById(id) == null)
            throw new EntityNotFoundException("Permission could not be found!");
        permissionRepository.deleteById(id);
        return true;
    }

    public Boolean hasPermission(User user, String permission){
        if(user.getRoles()==null)
            return false;
        for (Role role : user.getRoles()) {
            if (role.getPermissions() == null)
                return false;
            List<Permission> permissions = role.getPermissions().stream()
                    .filter((permission1 -> permission1.getType().equals(permission))).toList();
            if (permissions.size() > 0)
                return true;
        }
        return false;
    }

    public PermissionDTO findPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission == null)
            throw new EntityNotFoundException("Permission could not be found!");
        return Permission.mapToDTO(permission);
    }
}
