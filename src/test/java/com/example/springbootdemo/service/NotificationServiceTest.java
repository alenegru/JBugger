package com.example.springbootdemo.service;

import com.example.springbootdemo.SpringbootDemoApplication;
import com.example.springbootdemo.dto.BugDTO;
import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.dto.UserDTO;
import com.example.springbootdemo.model.Bug;
import com.example.springbootdemo.model.UserNotification;
import com.example.springbootdemo.model.enums.BugSeverity;
import com.example.springbootdemo.model.enums.BugStatus;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringbootDemoApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;


    @BeforeEach
    void SecurityContext(){
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    void TestNotificationUpdated() throws ValidationException {
        Bug bug=new Bug();
        BugDTO updatedBugDTO=new BugDTO();
        UserDTO userDTO=new UserDTO();

        userDTO.setIdUser(1L);
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("Silvia");
        userDTO.setLastName("Puiac");
        userDTO.setUsername("SilviaP");
        userDTO.setMobileNumber("+40749842955");
        List<RoleDTO> roles=new ArrayList<>();
        userDTO.setRoles(roles);
        UserDTO user=userService.addUser(userDTO);
        userService.activateUser(user.getIdUser());


        bug.setIdBug(1L);
        bug.setDescription("Internal Server Error");
        bug.setSeverity(BugSeverity.CRITICAL);
        bug.setStatus(BugStatus.FIXED);
        bug.setFixedVersion("100");
        bug.setTitle("ERROR 500");
        bug.setVersion("23");
        bug.setAssignedTo(userService.mapToUser(userDTO));
        bug.setCreatedBy(userService.mapToUser(userDTO));

        updatedBugDTO.setId(2L);
        updatedBugDTO.setDescription("Internal Server Error");
        updatedBugDTO.setSeverity(BugSeverity.CRITICAL);
        updatedBugDTO.setStatus(BugStatus.FIXED);
        updatedBugDTO.setFixedVersion("100");
        updatedBugDTO.setTitle("ERROR 500");
        updatedBugDTO.setVersion("25");
        updatedBugDTO.setAssignedTo(user.getUsername());
        updatedBugDTO.setCreatedBy(user.getUsername());

        List<UserNotification> userNotificationList=notificationService.addUserNotificationBugUpdated(bug,updatedBugDTO);
        assertNotNull(userNotificationList);
        assertEquals(1,userNotificationList.size());
        assertEquals("BUG_UPDATED",userNotificationList.get(0).getNotification().getType());


    }


    @Test
    @Transactional
    void TestNotificationClosed() throws ValidationException {

        Bug bug=new Bug();
        BugDTO updatedBugDTO=new BugDTO();
        UserDTO userDTO=new UserDTO();

        userDTO.setIdUser(1L);
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("Silvia");
        userDTO.setLastName("Puiac");
        userDTO.setMobileNumber("+40749842955");
        List<RoleDTO> roles=new ArrayList<>();
        userDTO.setRoles(roles);
        UserDTO user=userService.addUser(userDTO);
        userService.activateUser(user.getIdUser());


        bug.setIdBug(1L);
        bug.setDescription("Internal Server Error");
        bug.setSeverity(BugSeverity.CRITICAL);
        bug.setStatus(BugStatus.REJECTED);
        bug.setFixedVersion("100");
        bug.setTitle("ERROR 500");
        bug.setVersion("100");
        bug.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        bug.setAssignedTo(userService.mapToUser(userDTO));
        bug.setCreatedBy(userService.mapToUser(userDTO));

        updatedBugDTO.setId(1L);
        updatedBugDTO.setDescription("Internal Server Error");
        updatedBugDTO.setSeverity(BugSeverity.CRITICAL);
        updatedBugDTO.setStatus(BugStatus.CLOSED);
        updatedBugDTO.setFixedVersion("100");
        updatedBugDTO.setTitle("ERROR 500");
        updatedBugDTO.setVersion("100");
        updatedBugDTO.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        updatedBugDTO.setAssignedTo(userService.mapToUser(userDTO).getUsername());
        updatedBugDTO.setCreatedBy(userService.mapToUser(userDTO).getUsername());

        List<UserNotification> userNotificationList=notificationService.addUserNotificationBugUpdated(bug,updatedBugDTO);
        assertNotNull(userNotificationList);
        assertEquals(1,userNotificationList.size());
        assertEquals("BUG_CLOSED",userNotificationList.get(0).getNotification().getType());

    }

    @Test
    @Transactional
    void TestNotificationStatusUpdated() throws ValidationException {
        Bug bug=new Bug();
        BugDTO updatedBugDTO=new BugDTO();
        UserDTO userDTO=new UserDTO();


        userDTO.setIdUser(1L);
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("Silvia");
        userDTO.setLastName("Puiac");
        userDTO.setMobileNumber("+40749842955");
        List<RoleDTO> roles=new ArrayList<>();
        userDTO.setRoles(roles);
        UserDTO user=userService.addUser(userDTO);
        userService.activateUser(user.getIdUser());


        bug.setIdBug(1L);
        bug.setDescription("Internal Server Error");
        bug.setSeverity(BugSeverity.CRITICAL);
        bug.setStatus(BugStatus.IN_PROGRESS);
        bug.setFixedVersion("100");
        bug.setTitle("ERROR 500");
        bug.setVersion("100");
        bug.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        bug.setAssignedTo(userService.mapToUser(userDTO));
        bug.setCreatedBy(userService.mapToUser(userDTO));

        updatedBugDTO.setId(2L);
        updatedBugDTO.setDescription("Internal Server Error");
        updatedBugDTO.setSeverity(BugSeverity.CRITICAL);
        updatedBugDTO.setStatus(BugStatus.REJECTED);
        updatedBugDTO.setFixedVersion("100");
        updatedBugDTO.setTitle("ERROR 500");
        updatedBugDTO.setVersion("100");
        updatedBugDTO.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        updatedBugDTO.setAssignedTo(userService.mapToUser(userDTO).getUsername());
        updatedBugDTO.setCreatedBy(userService.mapToUser(userDTO).getUsername());

        List<UserNotification> userNotificationList=notificationService.addUserNotificationBugUpdated(bug,updatedBugDTO);
        assertNotNull(userNotificationList);
        assertEquals(1,userNotificationList.size());
        assertEquals("BUG_STATUS_UPDATED",userNotificationList.get(0).getNotification().getType());

    }



}