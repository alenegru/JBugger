package com.example.springbootdemo.service;

import com.example.springbootdemo.SpringbootDemoApplication;
import com.example.springbootdemo.dto.PermissionDTO;
import com.example.springbootdemo.model.Permission;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringbootDemoApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class PermissionServiceTest {
    @Autowired
    private PermissionService permissionService;

    @Test
    @Transactional
    void addAndRemoveTestPermission() {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setType("zxcgd");
        permissionDTO.setDescription("asdasda");
        PermissionDTO permission = permissionService.addPermission(permissionDTO);
        assertNotNull(permission);

        Boolean success = permissionService.removePermission(permission.getIdPermission());
        assertTrue(success);
    }


}