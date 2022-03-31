package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.PermissionDTO;
import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public List<RoleDTO> findAll() {
        return roleService.findAll();
    }

    @PostMapping("/roles/add")
    public RoleDTO addRole(@RequestBody RoleDTO roleDTO) {
        return roleService.addRole(roleDTO);
    }

    @DeleteMapping("/roles/delete/{id}")
    public Boolean deleteRoleById(@PathVariable Long id) {
        return roleService.removeRole(id);
    }

    @GetMapping("/roles/{id}")
    public RoleDTO findById(@PathVariable Long id) {
        return roleService.findRoleById(id);
    }

    @DeleteMapping("/roles/deletePermission/{id}/{id2}")
    public Boolean deleteRolePermissionById(@PathVariable Long id, @PathVariable Long id2) {
        return roleService.deletePermission(id, id2);
    }

    @PostMapping("/roles/addPermission/{idRole}/{idPerm}")
    public Boolean addRolePermission(@PathVariable Long idRole, @PathVariable Long idPerm) {
        return roleService.addPermissionToRole(idRole, idPerm);
    }

    @PostMapping("/roles/addPermissions/{id}")
    public List<Boolean> addRolePermission(@PathVariable Long id, @RequestBody List<PermissionDTO> permissions) {
        return roleService.addPermissionList(id, permissions);
    }

    @PostMapping("/roles/update")
    public Boolean updateRolePermissions(@RequestBody RoleDTO roleDTO) {
        return roleService.updateRole(roleDTO);
    }

    @DeleteMapping("/roles/removePermissions/{id}")
    public List<Boolean> removeRolePermission(@PathVariable Long id, @RequestBody List<PermissionDTO> permissions) {
        return roleService.removePermissionList(id, permissions);
    }
}
