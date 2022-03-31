package com.example.springbootdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="user_notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_user_notifications_gen")
    @SequenceGenerator(name = "my_user_notifications_gen", sequenceName = "my_user_notifications_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_id", referencedColumnName = "idNotification")
    private Notification notification;

    private Timestamp date;

    private Boolean read;
}