package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.BugDTO;
import com.example.springbootdemo.dto.NotificationDTO;
import com.example.springbootdemo.model.*;
import com.example.springbootdemo.model.enums.BugStatus;
import com.example.springbootdemo.repository.NotificationRepository;
import com.example.springbootdemo.repository.UserNotificationRepository;
import com.example.springbootdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@EnableScheduling
public class NotificationService {
    @Autowired
    private UserNotificationRepository userNotificationRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private PermissionService permissionService;



    public ResponseEntity<List<NotificationDTO>> findNotificationsByUser(String username) {
        User user = userRepository.findByUserName(username);
        List<UserNotification> userNotifications = userNotificationRepository.findUserNotificationsByUser(user);

        return new ResponseEntity<>(userNotifications.stream().map(this::mapToDTO).toList(), HttpStatus.OK);

    }

    public ResponseEntity<Integer> checkIfRead(String username) {
        User user = userRepository.findByUserName(username);
        int unreadNotifications=0;
        List<UserNotification> userNotifications = userNotificationRepository.findUserNotificationsByUser(user);
        for (UserNotification notification : userNotifications) {
            if (notification.getRead())
                unreadNotifications++;
        }
        return new ResponseEntity<>(unreadNotifications, HttpStatus.OK);
    }

    public Boolean changeStatusNotification(Long id) {
        UserNotification userNotification = userNotificationRepository.findUserNotificationById(id);
        userNotification.setRead(false);
        userNotificationRepository.save(userNotification);
        return true;

    }


    private UserNotification getStandardUserNotification(String username) {
        UserNotification userNotification = new UserNotification();
        userNotification.setRead(true);
        userNotification.setDate(new Timestamp(System.currentTimeMillis()));
        userNotification.setUser(userRepository.findByUserName(username));
        return userNotification;
    }

    @Transactional
    public List<UserNotification> addUserNotificationBugUpdated(Bug oldBug, BugDTO bugDTOUpdated) {
        List<UserNotification> userNotificationList = new ArrayList<>();
        boolean differentUsers = false;


        UserNotification userAssignedNotification = getStandardUserNotification(bugDTOUpdated.getAssignedTo());
        UserNotification userCreatedNotification=null;
        if(!bugDTOUpdated.getAssignedTo().equals(bugDTOUpdated.getCreatedBy())){
            differentUsers=true;
            userCreatedNotification = getStandardUserNotification(bugDTOUpdated.getCreatedBy());

        }

        UserNotification userAssignedNotificationForStatus = null;

        UserNotification userCreatedNotificationForStatus = null;

        if (oldBug.getStatus() != bugDTOUpdated.getStatus() && bugDTOUpdated.getStatus() != BugStatus.CLOSED) {
            Notification toSetNotification = getNotificationFor(bugDTOUpdated.getId(), "BUG_STATUS_UPDATED", "Bug status updated: " + bugDTOUpdated.getTitle() + "\nDetails: " + oldBug.getStatus() + " => " + bugDTOUpdated.getStatus());
            userAssignedNotificationForStatus=getStandardUserNotification(bugDTOUpdated.getAssignedTo());
            userAssignedNotificationForStatus.setNotification(toSetNotification);
            if(differentUsers){
                userCreatedNotificationForStatus=getStandardUserNotification(bugDTOUpdated.getCreatedBy());
                userCreatedNotificationForStatus.setNotification(toSetNotification);
                userNotificationRepository.save(userCreatedNotificationForStatus);
                userNotificationList.add(userCreatedNotificationForStatus);
            }
            userNotificationRepository.save(userAssignedNotificationForStatus);
            userNotificationList.add(userAssignedNotificationForStatus);
            historyService.addHistory(oldBug.getStatus(), bugDTOUpdated.getStatus(), oldBug.getIdBug());

        } else if (oldBug.getStatus() != BugStatus.CLOSED && bugDTOUpdated.getStatus() == BugStatus.CLOSED) {
            Notification toSetNotification = getNotificationFor(bugDTOUpdated.getId(), "BUG_CLOSED", "Bug closed: " + bugDTOUpdated.getTitle() + "\nPrevious status: "+ oldBug.getStatus());
            userAssignedNotificationForStatus=getStandardUserNotification(bugDTOUpdated.getAssignedTo());
            userAssignedNotificationForStatus.setNotification(toSetNotification);
            if(differentUsers){
                userCreatedNotificationForStatus=getStandardUserNotification(bugDTOUpdated.getCreatedBy());
                userCreatedNotificationForStatus.setNotification(toSetNotification);
                userNotificationRepository.save(userCreatedNotificationForStatus);
                userNotificationList.add(userCreatedNotificationForStatus);
            }
            userNotificationRepository.save(userAssignedNotificationForStatus);
            userNotificationList.add(userAssignedNotificationForStatus);
            historyService.addHistory(oldBug.getStatus(), bugDTOUpdated.getStatus(), oldBug.getIdBug());

        }


        if (!oldBug.getTitle().equals(bugDTOUpdated.getTitle()) || !oldBug.getFixedVersion().equals(bugDTOUpdated.getFixedVersion()) || !oldBug.getVersion().equals(bugDTOUpdated.getVersion()) ||
                !oldBug.getDescription().equals(bugDTOUpdated.getDescription()) || oldBug.getSeverity() != bugDTOUpdated.getSeverity() || !oldBug.getTargetDate().equals(bugDTOUpdated.getTargetDate()) ||
                !oldBug.getCreatedBy().getUsername().equals(bugDTOUpdated.getCreatedBy()) || !oldBug.getAssignedTo().getUsername().equals(bugDTOUpdated.getAssignedTo())
        ) {
            Notification toSetNotification = getNotificationFor(bugDTOUpdated.getId(), "BUG_UPDATED", generateMessageBugUpdated(bugDTOUpdated, oldBug));
            userAssignedNotification.setNotification(toSetNotification);
            if(differentUsers){
                userCreatedNotification.setNotification(toSetNotification);
                userNotificationRepository.save(userCreatedNotification);
                userNotificationList.add(userCreatedNotification);
            }
            userNotificationRepository.save(userAssignedNotification);
            userNotificationList.add(userAssignedNotification);

        }

        return userNotificationList;

    }

    private String generateMessageBugUpdated(BugDTO newBug, Bug oldBug) {
        StringBuilder str = new StringBuilder();
        str.append("Bug updated: ");
        str.append(oldBug.getTitle());
        if (!newBug.getTitle().equals(oldBug.getTitle())) {
            str.append(" => ").append(newBug.getTitle());
        }
        if (!newBug.getDescription().equals(oldBug.getDescription())) {
            str.append("\nDescription: ");
            str.append(oldBug.getDescription());
            str.append(" => ").append(newBug.getDescription());
        }
        if (!newBug.getVersion().equals(oldBug.getVersion())) {
            str.append("\nVersion: ");
            str.append(oldBug.getVersion());
            str.append(" => ").append(newBug.getVersion());
        }
        if (!newBug.getFixedVersion().equals(oldBug.getFixedVersion())) {
            str.append("\nFixed Version: ");
            str.append(oldBug.getFixedVersion());
            str.append(" => ").append(newBug.getFixedVersion());
        }
        if (!newBug.getSeverity().equals(oldBug.getSeverity())) {
            str.append("\nSeverity: ");
            str.append(oldBug.getSeverity());
            str.append(" => ").append(newBug.getSeverity());
        }
        if (!newBug.getStatus().equals(oldBug.getStatus())) {
            str.append("\nStatus: ");
            str.append(oldBug.getStatus());
            str.append(" => ").append(newBug.getStatus());

        }
        if (!newBug.getAssignedTo().equals(oldBug.getAssignedTo().getUsername())) {
            str.append("\nAssigned to: ");
            str.append(oldBug.getAssignedTo().getUsername());
            str.append(" => ").append(newBug.getAssignedTo());
        }
        return str.toString();
    }


    public NotificationDTO mapToDTO(UserNotification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setRead(notification.getRead());
        dto.setMessage(notification.getNotification().getMessage());
        dto.setType(notification.getNotification().getType());
        dto.setUrl(notification.getNotification().getUrl());
        return dto;
    }

    @Transactional
    public UserNotification addUserNotificationWelcomeNewUser(User user) {
        Notification toSetNotification = getNotificationFor(user.getIdUser(), "WELCOME_NEW_USER", "Welcome: " + user.getUsername());

        UserNotification userNotification = new UserNotification();
        userNotification.setNotification(toSetNotification);
        userNotification.setUser(user);
        userNotification.setRead(true);
        userNotification.setDate(new Timestamp(System.currentTimeMillis()));
        userNotificationRepository.save(userNotification);
        return userNotification;
    }

    public UserNotification addUserNotificationUserUpdated(User userNew, User userOld, String updater) {
        UserNotification userNotification = new UserNotification();
        Notification toSetNotification = getNotificationFor(userNew.getIdUser(), "USER_UPDATED", generateMessageUserUpdated(userNew, userOld, updater));
        userNotification.setNotification(toSetNotification);
        userNotification.setUser(userNew);
        userNotification.setRead(true);
        userNotification.setDate(new Timestamp(System.currentTimeMillis()));
        userNotificationRepository.save(userNotification);
        if (!userNew.getUsername().equals(updater)) {
            User updaterUser = userRepository.findByUserName(updater);
            UserNotification updaterNotification = new UserNotification();
            updaterNotification.setNotification(toSetNotification);
            updaterNotification.setUser(updaterUser);
            updaterNotification.setRead(true);
            updaterNotification.setDate(new Timestamp(System.currentTimeMillis()));
            userNotificationRepository.save(updaterNotification);
        }
        return userNotification;
    }

    private String generateMessageUserUpdated(User userNew, User userOld, String updater) {
        StringBuilder str = new StringBuilder();
        str.append("User updated: ");
        str.append(userNew.getUsername());
        if (!userNew.getFirstName().equals(userOld.getFirstName())) {
            str.append("\nFirst name: ");
            str.append(userOld.getFirstName());
            str.append(" => ").append(userNew.getFirstName());
        }
        if (!userNew.getLastName().equals(userOld.getLastName())) {
            str.append("\nLast name: ");
            str.append(userOld.getLastName());
            str.append(" => ").append(userNew.getLastName());
        }
        if (!userNew.getMobileNumber().equals(userOld.getMobileNumber())) {
            str.append("\nMobile number: ");
            str.append(userOld.getMobileNumber());
            str.append(" => ").append(userNew.getMobileNumber());
        }
        if (!userNew.getEmail().equals(userOld.getEmail())) {
            str.append("\nEmail: ");
            str.append(userOld.getEmail());
            str.append(" => ").append(userNew.getEmail());
        }
        str.append("\nUpdated by: ").append(updater);
        return str.toString();
    }

    @Transactional
    public List<UserNotification> addUserNotificationUserDeactivated(User userDeact) {
        List<User> users = (List<User>) userRepository.findAll();
        users = users.stream().filter((user) -> permissionService.hasPermission(user, "USER_MANAGEMENT")).toList();
        List<UserNotification> userNotifications = new ArrayList<>();
        Notification toSetNotification = getNotificationFor(userDeact.getIdUser(), "USER_DEACTIVATED", "Deactivated: " + userDeact.getUsername());
        for (User user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setUser(user);
            userNotification.setNotification(toSetNotification);
            userNotification.setRead(true);
            userNotification.setDate(new Timestamp(System.currentTimeMillis()));
            userNotificationRepository.save(userNotification);
            userNotifications.add(userNotification);
        }
        return userNotifications;
    }

    private Notification getNotificationFor(Long id, String type, String message) {
        List<Notification> notificationList = notificationRepository.findNotificationByType(type).orElse(null);
        String url = "/users/";
        int start = 7;
        if (type.contains("BUG")) {
            url = "/bugs/";
            --start;
            if(type.contains("STATUS") || type.contains("CLOSED")){
                url+="history/";
                start=14;
            }
        }
        Notification toSetNotification = null;
        if (notificationList == null) {
            toSetNotification = new Notification();
            toSetNotification.setMessage(message);
            toSetNotification.setType(type);
            toSetNotification.setUrl(url + id);
            notificationRepository.save(toSetNotification);
        } else {
            for (Notification not : notificationList) {
                if(start>not.getUrl().length())
                    continue;
                if (not.getUrl().substring(start).equals(id.toString())) {
                    not.setMessage(message);
                    toSetNotification = not;
                    break;
                }
            }
            if (toSetNotification == null) {
                toSetNotification = new Notification();
                toSetNotification.setMessage(message);
                toSetNotification.setType(type);
                toSetNotification.setUrl(url + id);
                notificationRepository.save(toSetNotification);
            }
        }
        return toSetNotification;
    }


    @Scheduled(fixedRate = 300000/*, initialDelay = 50000*/)
    protected void deleteOldNotifications() {
        List<UserNotification> user_notifications = (List<UserNotification>) userNotificationRepository.findAll();
        for (UserNotification notification : user_notifications) {
            if (notification.getDate().toLocalDateTime().toLocalDate()
                    .isBefore(LocalDate.now().minusDays(30))) {
                userNotificationRepository.delete(notification);
            }
        }
    }
}