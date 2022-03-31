package com.example.springbootdemo.repository;

import com.example.springbootdemo.model.History;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface HistoryRepository extends CrudRepository<History, Long> {

}
