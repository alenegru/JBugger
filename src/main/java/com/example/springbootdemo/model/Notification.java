package com.example.springbootdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mynotifications_gen")
    @SequenceGenerator(name = "mynotifications_gen", sequenceName = "mynotifications_seq", allocationSize = 1)
    private Long idNotification;
    private String Type;
    private String url;
    private String message;

    @OneToMany(mappedBy = "notification")
    private List<UserNotification> userNotificationList;


}
