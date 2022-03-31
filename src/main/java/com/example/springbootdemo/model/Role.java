package com.example.springbootdemo.model;

import com.example.springbootdemo.dto.RoleDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_gen")
    @SequenceGenerator(name = "roles_gen", sequenceName = "roles_seq", allocationSize = 1)
    private Long idRole;
    @Column(unique = true)
    private String type;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable( name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id_role"),
            inverseJoinColumns = @JoinColumn(name = "permissions_id_permission"))
    private List<Permission> permissions;

    public static Role mapToRole(RoleDTO roleDTO){
        Role role=new Role();
        role.setType(roleDTO.getType());
        role.setIdRole(roleDTO.getIdRole());
        if(roleDTO.getPermissions()!=null) {
            role.setPermissions((roleDTO.getPermissions().stream().map(Permission::mapToPermission).toList()));
        }
        else{
            role.setPermissions(new ArrayList<>());
        }
        return role;
    }
    public static RoleDTO mapToDTO(Role role){
        RoleDTO dto=new RoleDTO();
        dto.setType(role.getType());
        dto.setIdRole(role.getIdRole());
        if(role.getPermissions()!=null) {
            dto.setPermissions(role.getPermissions().stream().map(Permission::mapToDTO).toList());
        }
        else{
            dto.setPermissions(new ArrayList<>());
        }
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return type.equals(role.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
