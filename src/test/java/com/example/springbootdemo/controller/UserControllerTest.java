package com.example.springbootdemo.controller;


import com.example.springbootdemo.SpringbootDemoApplication;
import com.example.springbootdemo.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    @Transactional
    public void addRemoveUserTest() throws Exception {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        ResponseEntity<UserDTO> resp = userController.addUser(userDTO);
        assertNotNull(resp);
        assertTrue(resp.getStatusCode() == HttpStatus.OK);
        ResponseEntity<UserDTO> resp2 = userController.findById(resp.getBody().getIdUser());
        assertTrue(resp2.getBody().getFirstName().equals("asd"));
        Boolean success=userController.deleteUserById(resp.getBody().getIdUser());
        assertTrue(success);
    }

    @Test
    @Transactional
    public void updateUserTest() throws Exception {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        ResponseEntity<UserDTO> resp = userController.addUser(userDTO);
        assertNotNull(resp);
        userDTO.setIdUser(resp.getBody().getIdUser());
        userDTO.setFirstName("wewewe");
        userController.updateUser(userDTO);
        resp = userController.findById(resp.getBody().getIdUser());
        assertTrue(resp.getBody().getFirstName().equals("wewewe"));
    }



}
