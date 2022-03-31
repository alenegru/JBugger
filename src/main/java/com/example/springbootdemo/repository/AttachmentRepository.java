package com.example.springbootdemo.repository;

import com.example.springbootdemo.model.Attachment;
import com.example.springbootdemo.model.Bug;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;

@Repository
@ApplicationScope
public interface AttachmentRepository extends CrudRepository<Attachment, Long> {
    @Query("select a from Attachment a where a.bug.idBug=:id")
    Optional<List<Attachment>> findByBug(@Param("id")Long id);
}
