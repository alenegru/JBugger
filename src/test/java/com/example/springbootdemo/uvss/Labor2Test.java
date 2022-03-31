package com.example.springbootdemo.uvss;

import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class Labor2Test {
    public UserDTO user0;
    public UserDTO user1;
    public UserDTO user2;
    public UserDTO user3;
    public UserDTO user4;

    @BeforeEach
    public void populateData() {

        RoleDTO roleADM = new RoleDTO();
        roleADM.setType("ADM");
        RoleDTO roleDEV = new RoleDTO();
        roleDEV.setType("DEV");
        RoleDTO roleTEST = new RoleDTO();
        roleTEST.setType("TEST");
        RoleDTO roleTM = new RoleDTO();
        roleTM.setType("TM");
        RoleDTO rolePM = new RoleDTO();
        rolePM.setType("PM");

        List<RoleDTO> oneRole = new ArrayList<>();
        oneRole.add(roleADM);
        List<RoleDTO> threeRoles = new ArrayList<>();
        threeRoles.add(roleADM);
        threeRoles.add(roleDEV);
        threeRoles.add(roleTM);
        List<RoleDTO> twoRoles = new ArrayList<>();
        twoRoles.add(roleDEV);
        twoRoles.add(roleTM);
        List<RoleDTO> fiveRoles = new ArrayList<>();
        fiveRoles.add(roleADM);
        fiveRoles.add(roleDEV);
        fiveRoles.add(roleTEST);
        fiveRoles.add(roleTM);
        fiveRoles.add(rolePM);

        user0=new UserDTO();
        user0.setEmail("Silvia.Puiac@msg.group");
        user0.setFirstName("asd");
        user0.setLastName("asd");
        user0.setMobileNumber("+40749842955");
        user0.setRoles(new ArrayList<>());

        user1=new UserDTO();
        user1.setEmail("Silvia.Puiac@msg.group");
        user1.setFirstName("asd");
        user1.setLastName("asd");
        user1.setMobileNumber("+40749842955");
        user1.setRoles(oneRole);


        user2=new UserDTO();
        user2.setEmail("Silvia.Puiac@msg.group");
        user2.setFirstName("asd");
        user2.setLastName("asd");
        user2.setMobileNumber("+40749842955");
        user2.setRoles(twoRoles);


        user3=new UserDTO();
        user3.setEmail("Silvia.Puiac@msg.group");
        user3.setFirstName("asd");
        user3.setLastName("asd");
        user3.setMobileNumber("+40749842955");
        user3.setRoles(threeRoles);

        user4=new UserDTO();
        user4.setEmail("Silvia.Puiac@msg.group");
        user4.setFirstName("asd");
        user4.setLastName("asd");
        user4.setMobileNumber("+40749842955");
        user4.setRoles(fiveRoles);
    }

    //ECP
    //Intervalle
    // <0 -> invalid
    //[0,3] -> valid
    //4 -> invalid
    //5 -> valid
    // >5 -> invalid

    @Test
    public void ecpInvalidNrofRoles() {
        assertNotEquals(-2, user0.getRoles().size());
    }

    @Test
    public void ecpValidWithThreeRoles() {
        assertEquals(3, user3.getRoles().size());
    }

    @Test
    public void ecpValidWithTwoRoles() {
        assertEquals(2, user2.getRoles().size());
    }

    @Test
    public void ecpInvalidWithFourRoles() {
        assertNotEquals(4, user2.getRoles().size());
    }

    @Test
    public void ecpValidWithFiveRoles() {
        assertEquals(5, user4.getRoles().size());
    }

    @Test
    public void ecpInvalidNrofRolesHigh() {
        assertNotEquals(10, user4.getRoles().size());
    }


    //BVA
    //0 - invalid
    //3 - valid
    //4 - invalid
    //5 - valid

    @Test
    public void bvaInvalidWithNoRoles() {
        assertFalse(user0.getRoles().size()==5);
    }

    @Test
    public void bvaInvalidWithNoRoles2() {
        assertFalse(user2.getRoles().size()==0);
    }

    @Test
    public void bvaValidWithTwoRoles() {
        assertTrue(user2.getRoles().size()==2);
    }

    @Test
    public void bvaValidWithThreeRoles() {
        assertTrue(user3.getRoles().size()==3);
    }

    @Test
    public void bvaInvalidWithFourRoles() {
        assertFalse(user0.getRoles().size()==4);
    }

    @Test
    public void bvaValidWithFiveRoles() {
        assertTrue(user4.getRoles().size()==5);
    }
}