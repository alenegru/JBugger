package com.example.springbootdemo.repository;

import com.example.springbootdemo.model.Bug;
import com.example.springbootdemo.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@ApplicationScope
public interface BugRepository extends CrudRepository<Bug, Long> {
    @Query("select b from Bug b where b.title=:title")
    Bug findByTitle(@Param("title")String title);

    @Query("select b from Bug b where b.assignedTo=:id")
    List<Bug> returnAllAssignedTo(@Param("id")User id);

    @Query("select b from Bug b where b.createdBy.username=:username")
    List<Bug> findBugsCreatedBy(@Param("username")String username);

    @Query("select b from Bug b where b.assignedTo.username=:username")
    List<Bug> findBugsAssignedTo(@Param("username")String username);


}
