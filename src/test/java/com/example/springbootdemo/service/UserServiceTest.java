package com.example.springbootdemo.service;

import com.example.springbootdemo.SpringbootDemoApplication;
import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.dto.UserDTO;
import com.example.springbootdemo.model.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringbootDemoApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Test
    @Transactional
    void addAndRemoveTestUser() throws Exception {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        UserDTO user=userService.addUser(userDTO);
        assertNotNull(user);

        assertTrue(userService.removeUser(user.getIdUser()));
    }

    @Test
    @Transactional

    void updateUserTest() throws Exception {
        //test 1
        UserDTO userDTO = new UserDTO();
        List<RoleDTO> roles = new ArrayList<>();
        userDTO.setEmail("Stefana-Georgiana.Rosca@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("(+40)-770-000000");
        userDTO.setRoles(roles);
        UserDTO user=userService.addUser(userDTO);
        System.out.println(user.getPassword()+"WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY"+user+"\n");
        //userService.mapToUser(user);
        System.out.println(user.getPassword()+"WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
        System.out.println(user.getUsername()+"AAAAAAAAAAAAAAAAAAAAAAAAAAA");

        loginService.login(user.getUsername(),"asdasd");
        UserDTO userWithUpdates=new UserDTO();
        userWithUpdates.setEmail("Silvia.Puiac@msg.group");
        userWithUpdates.setFirstName("Stefana");
        userWithUpdates.setLastName("Rosca");
        userWithUpdates.setMobileNumber("(+40)-770-000000");

        //test 2
        UserDTO userDTO2=new UserDTO();
        userDTO2.setEmail("Silvia.Puiac@msg.group");
        userDTO2.setFirstName("asd");
        userDTO2.setLastName("asd");
        userDTO2.setMobileNumber("+40749842955");
        UserDTO user2=userService.addUser(userDTO2);
        assertTrue(userService.findUserById(user2.getIdUser()).getFirstName().equals("asd"));
        user2.setFirstName("ulu");
        userService.update(user2);


        //assertNotEquals(userDTO, userWithUpdates);
    }

    @Test
    @Transactional
    void addRemoveUserRoleTest() throws ValidationException {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        UserDTO user=userService.addUser(userDTO);
        Boolean success=userService.addRoleById(user.getIdUser(), 1L);
        assertTrue(success);
        assertFalse(userService.findAllRoles(user.getIdUser()).isEmpty());
        success = userService.deleteRole(user.getIdUser(), 1L);
        assertTrue(success);

    }

    @Test
    @Transactional
    void activateAndDeactivateTest() throws ValidationException {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("dwdwa@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        UserDTO user=userService.addUser(userDTO);
        Long id = user.getIdUser();
        userService.activateUser(id);
        Boolean success=(userService.findUserById(id).getStatus() == UserStatus.ACTIVE) ? true : false;
        assertTrue(success);
        userService.deactivateUser(id);
        success=(userService.findUserById(id).getStatus() == UserStatus.INACTIVE) ? true : false;
        assertTrue(success);

    }
}