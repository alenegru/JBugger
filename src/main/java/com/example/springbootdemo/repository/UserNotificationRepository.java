package com.example.springbootdemo.repository;

import com.example.springbootdemo.model.Notification;
import com.example.springbootdemo.model.User;
import com.example.springbootdemo.model.UserNotification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Set;
@Repository
@ApplicationScope
public interface UserNotificationRepository extends CrudRepository<UserNotification, Long> {
    @Query("select un from UserNotification un where un.user=:user")
    List<UserNotification> findUserNotificationsByUser(@Param("user")User user);

    @Query("select un from UserNotification un where un.id=:id")
    UserNotification findUserNotificationById(@Param("id")Long id);
}
