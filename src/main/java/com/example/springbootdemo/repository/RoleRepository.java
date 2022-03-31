package com.example.springbootdemo.repository;

import com.example.springbootdemo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByType(String type);
}
