package com.example.springbootdemo.repository;

import com.example.springbootdemo.model.Permission;
import com.example.springbootdemo.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface PermissionRepository extends CrudRepository<Permission, Long> {


    @Query("select p.idPermission from Permission p where p.type=:type")
    Long findIdByType(@Param("type")String type);
}
