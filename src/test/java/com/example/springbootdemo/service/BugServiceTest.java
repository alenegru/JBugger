package com.example.springbootdemo.service;

import com.example.springbootdemo.SpringbootDemoApplication;
import com.example.springbootdemo.dto.BugDTO;
import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.dto.UserDTO;
import com.example.springbootdemo.model.Attachment;
import com.example.springbootdemo.model.AttachmentInfoDto;
import com.example.springbootdemo.model.Bug;
import com.example.springbootdemo.model.User;
import com.example.springbootdemo.model.enums.BugSeverity;
import com.example.springbootdemo.model.enums.BugStatus;
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
class BugServiceTest {
    @Autowired
    private BugService bugService;

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
    void addAndRemoveTestBug() throws Exception {
        BugDTO bugDTO = new BugDTO();
        UserDTO userDTO = new UserDTO();
        List<RoleDTO> roles = new ArrayList<>();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        userDTO.setRoles(roles);
        UserDTO user = userService.addUser(userDTO);
        userService.activateUser(user.getIdUser());

        bugDTO.setDescription("asdasda");
        bugDTO.setSeverity(BugSeverity.CRITICAL);
        bugDTO.setTitle("titleTitle");
        bugDTO.setVersion("23");
        bugDTO.setAssignedTo(user.getUsername());
        bugDTO.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        BugDTO bug = bugService.addBug(bugDTO, user.getUsername());
        assertNotNull(bug);
        Long id = bugService.getIdByTitle(bug.getTitle());
        Boolean success = bugService.removeBug(id);
        assertTrue(success);
    }

    @Test
    @Transactional
    void updateTestBug() throws Exception {
        BugDTO bugDTO = new BugDTO();
        BugDTO bugDTO2 = new BugDTO();
        UserDTO userDTO = new UserDTO();

        List<RoleDTO> roles = new ArrayList<>();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        userDTO.setRoles(roles);
        UserDTO user = userService.addUser(userDTO);
        userService.activateUser(user.getIdUser());

        bugDTO.setDescription("asdasda");
        bugDTO.setSeverity(BugSeverity.CRITICAL);
        bugDTO.setTitle("titleTitle");
        bugDTO.setVersion("23");
        bugDTO.setFixedVersion("ok");
        bugDTO.setStatus(BugStatus.FIXED);
        bugDTO.setAssignedTo(user.getUsername());
        bugDTO.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));

        bugDTO=bugService.addBug(bugDTO, user.getUsername());
        assertNotNull(bugDTO);



        System.out.println(bugDTO+"INAINTE DE UPDATE");
        bugDTO2.setDescription("new");
        bugDTO2.setSeverity(BugSeverity.CRITICAL);
        bugDTO2.setTitle("new");
        bugDTO2.setVersion("23");
        bugDTO2.setFixedVersion("ok");
        bugDTO2.setStatus(BugStatus.FIXED);
        bugDTO2.setId(bugDTO.getId());
        bugDTO2.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        bugDTO2.setAssignedTo(user.getUsername());

        Bug bug=bugService.mapToBug(bugDTO);
        System.out.println(bug+"buggggggggggg");
        System.out.println(bugService.mapToBug(bugDTO)+"BUGGGGGGGGG");
        //bugService.updateById(bugDTO.getId(), bugDTO2);
//        assertNotEquals(bugDTO, bugDTO2);
//        assertNotEquals(bugDTO, bugDTO2);
    }

    @Test
    @Transactional
    void findByTitleOrIdTest() throws Exception {
        Bug bug =new Bug();
        BugDTO bugDTO = new BugDTO();
        UserDTO userDTO = new UserDTO();

        List<RoleDTO> roles = new ArrayList<>();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        userDTO.setRoles(roles);
        UserDTO user = userService.addUser(userDTO);
        userService.activateUser(user.getIdUser());

        bugDTO.setDescription("asdasda");
        bugDTO.setSeverity(BugSeverity.CRITICAL);
        bugDTO.setTitle("titleTitle");
        bugDTO.setVersion("23");
        bugDTO.setAssignedTo(user.getUsername());
        bugDTO.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        bugService.addBug(bugDTO, user.getUsername());

        assertNotNull(bugService.findBugByTitle(bugDTO.getTitle()));
        bug.setIdBug(bugService.getIdByTitle(bugDTO.getTitle()));
        assertNotNull(bugService.findBugById(bug.getIdBug()));
    }

    @Test
    @Transactional
    void findAssignedToCreatedByTest() throws Exception {
        BugDTO bugDTO = new BugDTO();
        UserDTO userDTO = new UserDTO();

        List<RoleDTO> roles = new ArrayList<>();
        userDTO.setEmail("Silvia.Puiac@msg.group");
        userDTO.setFirstName("asd");
        userDTO.setLastName("asd");
        userDTO.setMobileNumber("+40749842955");
        userDTO.setRoles(roles);
        UserDTO user = userService.addUser(userDTO);
        userService.activateUser(user.getIdUser());

        bugDTO.setDescription("asdasda");
        bugDTO.setSeverity(BugSeverity.CRITICAL);
        bugDTO.setTitle("titleTitle");
        bugDTO.setVersion("23");
        bugDTO.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        bugService.addBug(bugDTO, user.getUsername());

        assertNotNull(bugService.findBugsAssignedTo(bugDTO.getAssignedTo()));
        assertNotNull(bugService.findBugsCreatedBy(bugDTO.getCreatedBy()));
        assertEquals(bugService.findBugsAssignedTo(bugDTO.getAssignedTo()), bugService.findBugsCreatedBy(bugDTO.getCreatedBy()));

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setEmail("Silvia.Puiac@msg.group");
        userDTO2.setFirstName("stf");
        userDTO2.setLastName("rosca");
        userDTO2.setMobileNumber("+40749842955");
        userDTO2.setRoles(roles);
        UserDTO user2 = userService.addUser(userDTO2);
        userService.activateUser(user2.getIdUser());

        BugDTO bugDTO2 = bugService.findBugByTitle(bugDTO.getTitle());
        bugDTO2.setDescription("descr");
        bugDTO2.setSeverity(BugSeverity.CRITICAL);
        bugDTO2.setTitle("title");
        bugDTO2.setVersion("23");
        bugDTO2.setAssignedTo(user2.getUsername());
        bugDTO2.setTargetDate(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"));
        bugService.addBug(bugDTO2, user.getUsername());
        System.out.println(bugDTO2+"ASS SI CREATE DIF");


        assertNotNull(bugService.findBugsAssignedTo(bugDTO2.getAssignedTo()));
        assertNotNull(bugService.findBugsCreatedBy(bugDTO2.getCreatedBy()));
        assertNotEquals(bugDTO2.getAssignedTo(),bugDTO2.getCreatedBy());
        assertEquals(bugService.findBugsCreatedBy(bugDTO2.getCreatedBy()),bugService.findBugsAssignedTo(bugDTO2.getAssignedTo()));
    }


}