package com.example.springbootdemo.repository;

import com.example.springbootdemo.model.Comment;
import com.example.springbootdemo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface CommentRepository extends CrudRepository<Comment, Long> {
}
