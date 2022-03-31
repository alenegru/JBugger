package com.example.springbootdemo.repository;
import com.example.springbootdemo.model.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;

@Repository
@ApplicationScope
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    @Query("select n from Notification n where n.idNotification=:id")
    Notification findNotificationById(@Param("id")Long id);
    @Query("select n from Notification n where n.Type=:type")
    Optional<List<Notification>> findNotificationByType(@Param("type")String type);
}
