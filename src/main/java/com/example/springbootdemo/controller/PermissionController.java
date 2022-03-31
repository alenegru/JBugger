package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.PermissionDTO;
import com.example.springbootdemo.model.Permission;
import com.example.springbootdemo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permissions/")
    public List<PermissionDTO> findAll() {
        return permissionService.findAll();
    }

    @PostMapping("/permissions/add")
    public PermissionDTO addPermission(@RequestBody PermissionDTO permissionDTO) {
        return permissionService.addPermission(permissionDTO);
    }

    @DeleteMapping("/permissions/delete/{id}")
    public Boolean deletePermissionById(@PathVariable Long id) {
        return permissionService.removePermission(id);
    }

    @GetMapping("/permissions/{id}")
    public PermissionDTO findById(@PathVariable Long id) {
        return permissionService.findPermissionById(id);
    }
}
