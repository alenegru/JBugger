package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.PermissionDTO;
import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.model.Permission;
import com.example.springbootdemo.model.Role;
import com.example.springbootdemo.repository.PermissionRepository;
import com.example.springbootdemo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PermissionRepository permissionRepository;


    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(Role::mapToDTO).toList();
    }

    public Boolean removeRole(Long id) {
        if (findRoleById(id) == null)
            throw new EntityNotFoundException("Role could not be found!");
        roleRepository.deleteById(id);
        return true;
    }

    public Boolean removeRoleByType(String type) {
        Role role = roleRepository.findByType(type);

        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");
        roleRepository.deleteById(role.getIdRole());
        return true;
    }

    @Transactional
    public RoleDTO addRole(RoleDTO roleDTO) {
        Role role = Role.mapToRole(roleDTO);
        if (!role.getType().equals(""))
            roleRepository.save(role);
        return Role.mapToDTO(role);
    }

    @Transactional
    //Returns true if successful, false otherwise
    public Boolean updateRole(RoleDTO roleDTO) {

        Role role = Role.mapToRole(roleDTO);
        Role oldRole = roleRepository.findByType(roleDTO.getType());
        if (oldRole != null) {
            role.setIdRole(oldRole.getIdRole());
            //Find the reference to the actual permissions (if this is omitted
            //it will just create new permissions)
            for (Permission perm : role.getPermissions()) {
                perm.setIdPermission(permissionRepository.findIdByType(perm.getType()));
            }

            roleRepository.save(role);
            return true;
        }

        return false;
    }

    public RoleDTO findRoleById(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");
        return Role.mapToDTO(role);

    }

    public RoleDTO findRoleByType(String type) {
        Role role = roleRepository.findByType(type);
        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");
        return Role.mapToDTO(role);
    }

    @Transactional
    public Boolean deletePermission(Long roleId, Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId).orElse(null);
        if (permission == null)
            throw new EntityNotFoundException("Permission could not be found!");
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");
        role.getPermissions().remove(permission);
        roleRepository.save(role);
        return true;
    }

    @Transactional
    public Boolean addPermissionToRole(Long idRole, Long idPerm) {
        Role role = roleRepository.findById(idRole).orElse(null);
        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");
        Permission permission = permissionRepository.findById(idPerm).orElse(null);
        if (permission == null)
            throw new EntityNotFoundException("Permission could not be found!");

        role.getPermissions().add(permission);
        roleRepository.save(role);
        return true;

    }


    @Transactional
    public List<Boolean> addPermissionList(Long id, List<PermissionDTO> permissions) {
        List<Boolean> success = new ArrayList<>();
        for (PermissionDTO permission : permissions) {
            success.add(addPermissionToRole(id, permissionRepository.findIdByType(permission.getType())));
        }

        return success;
    }

    @Transactional
    public List<Boolean> removePermissionList(Long id, List<PermissionDTO> permissions) {
        List<Boolean> success = new ArrayList<>();
        for (PermissionDTO permission : permissions) {
            success.add(deletePermission(id, permissionRepository.findIdByType(permission.getType())));
        }

        return success;
    }
}
