package com.example.springbootdemo.service;

import com.example.springbootdemo.SpringbootDemoApplication;
import com.example.springbootdemo.dto.PermissionDTO;
import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.model.Permission;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringbootDemoApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class RoleServiceTest {
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Test
    @Transactional
    void addAndRemoveTestRole() {
        RoleDTO roledto = new RoleDTO();
        roledto.setType("zxcgd");
        RoleDTO role = roleService.addRole(roledto);
        assertNotNull(role);

        Boolean success = roleService.removeRoleByType(role.getType());
        assertTrue(success);
    }

    @Test
    @Transactional
    void updateRoleTest() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setType("ffff444");
        roleService.addRole(roleDTO);
        List<PermissionDTO> list = new ArrayList<>();
        PermissionDTO perm = new PermissionDTO();
        perm.setType("Uhu");
        list.add(perm);
        roleDTO.setPermissions(list);

        roleService.updateRole(roleDTO);

        RoleDTO roleCheck = roleService.findRoleByType(roleDTO.getType());
        assertNotNull(roleCheck.getPermissions());
    }

    @Test
    @Transactional
    void addRemovePermissionTest(){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setType("ffff444");
        roleDTO = roleService.addRole(roleDTO);
        List<PermissionDTO> list = new ArrayList<>();
        PermissionDTO perm = new PermissionDTO();
        perm.setType("Uhu");
        perm.setDescription("feafe");
        list.add(perm);
        perm = permissionService.addPermission(perm);
        roleService.addPermissionToRole(roleDTO.getIdRole(), perm.getIdPermission());
    }

    @Test
    @Transactional
    void getAllRolesTest() {
        List<RoleDTO> success = roleService.findAll();
        assertNotNull(success);
    }

    @Test
    @Transactional
    void getRoleTest() {
        RoleDTO success = roleService.findRoleById(1L);
        assertNotNull(success);
        RoleDTO roledto = new RoleDTO();
        roledto.setType("zxcgd");
        roleService.addRole(roledto);
        success = roleService.findRoleByType("zxcgd");
    }


}
