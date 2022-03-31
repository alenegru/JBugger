package com.example.springbootdemo.service;

import com.example.springbootdemo.SpringbootDemoApplication;
import com.example.springbootdemo.dto.CommentDTO;
import com.example.springbootdemo.dto.PermissionDTO;
import com.example.springbootdemo.model.Comment;
import com.example.springbootdemo.model.Permission;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.Timestamp;
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
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Test
    @Transactional
    void addAndRemoveTestComment(){
        CommentDTO commentDTO=new CommentDTO();
        commentDTO.setText("texttetet");
        commentDTO.setDate(new Timestamp(45));
        Comment comment=commentService.addComment(commentDTO);
        assertNotNull(comment);

        Boolean success=commentService.removeComment(comment.getIdComment());
        assertTrue(success);
    }


    @Test
    @Transactional
    void getAllPermissionsTest(){
        List<CommentDTO> success=commentService.findAll();
    }

    @Test
    @Transactional
    void getPermissionTest(){
        CommentDTO success=commentService.findCommentById(1L);
    }


}