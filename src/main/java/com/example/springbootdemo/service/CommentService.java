package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.CommentDTO;
import com.example.springbootdemo.model.Comment;
import com.example.springbootdemo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EntityManager entityManager;

    private Comment mapToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setDate(commentDTO.getDate());
        comment.setText(commentDTO.getText());
        return comment;
    }

    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setDate(comment.getDate());
        dto.setText(comment.getText());
        return dto;
    }

    public List<CommentDTO> findAll() {
        List<Comment> comments = (List<Comment>) commentRepository.findAll();
        return comments.stream().map(this::mapToDTO).toList();
    }

    @Transactional
    public Comment addComment(CommentDTO commentDTO) {
        Comment comment = mapToComment(commentDTO);
        if (!comment.getDate().equals(null) && !comment.getText().equals(""))
            commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public Boolean removeComment(Long id) {
        if (findCommentById(id) == null)
            return false;
        commentRepository.deleteById(id);
        return true;
    }

    public CommentDTO findCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            return null;
        return mapToDTO(comment);
    }
}
