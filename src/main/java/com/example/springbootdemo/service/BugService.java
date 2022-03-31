package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.BugDTO;
import com.example.springbootdemo.email.EmailSender;
import com.example.springbootdemo.error.exceptions.AssignedUserNullException;
import com.example.springbootdemo.error.exceptions.PermissionNotFoundException;
import com.example.springbootdemo.error.exceptions.UserInactiveException;
import com.example.springbootdemo.model.Bug;
import com.example.springbootdemo.model.User;
import com.example.springbootdemo.model.enums.BugStatus;
import com.example.springbootdemo.model.enums.UserStatus;
import com.example.springbootdemo.repository.BugRepository;
import com.example.springbootdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BugService {
    @Autowired
    private BugRepository bugRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private PermissionService permissionService;

    //TODO: save bug show proper exception
    public Bug mapToBug(BugDTO dto){
        Bug bug=new Bug();
        bug.setDescription(dto.getDescription());
        bug.setFixedVersion(dto.getFixedVersion());
        bug.setStatus(dto.getStatus());
        bug.setTargetDate(dto.getTargetDate());
        bug.setTitle(dto.getTitle());
        bug.setSeverity(dto.getSeverity());
        bug.setVersion(dto.getVersion());
        bug.setIdBug(dto.getId());
        return bug;
    }

    private BugDTO mapToDTO(Bug bug){
        BugDTO bugDTO=new BugDTO();
        bugDTO.setDescription(bug.getDescription());
        bugDTO.setFixedVersion(bug.getFixedVersion());
        bugDTO.setStatus(bug.getStatus());
        bugDTO.setTargetDate(bug.getTargetDate());
        bugDTO.setId(bug.getIdBug());
        bugDTO.setTitle(bug.getTitle());
        bugDTO.setSeverity(bug.getSeverity());
        bugDTO.setVersion(bug.getVersion());
        if (bug.getAssignedTo() != null)
            bugDTO.setAssignedTo(bug.getAssignedTo().getUsername());
        bugDTO.setCreatedBy(bug.getCreatedBy().getUsername());
        return bugDTO;
    }

    public List<BugDTO> findAll() {
        List<Bug> bugs = (List<Bug>) bugRepository.findAll();
        return bugs.stream().map(this::mapToDTO).toList();
    }

    @Transactional
    public BugDTO addBug(BugDTO bugDTO, String username) throws ValidationException {
        Bug bug = mapToBug(bugDTO);
        User userCreatedBy = userRepository.findByUserName(username);
        //User can't add bug if he's inactive
        if(userCreatedBy.getStatus().equals(UserStatus.INACTIVE) ){
            throw new UserInactiveException("User is inactive ");
        }

        User userAssignedTo = userRepository.findByUserName(bugDTO.getAssignedTo());

        bug.setCreatedBy(userCreatedBy);

        if (userAssignedTo == null)
            bug.setAssignedTo(userCreatedBy);
        else
            bug.setAssignedTo(userAssignedTo);

        bug.setStatus(BugStatus.OPEN);
        bug.setSeverity(bugDTO.getSeverity());

        if (!bug.getDescription().equals("") && !bug.getTitle().equals("")
                && bug.getCreatedBy() != null && bug.getSeverity() != null && bug.getTargetDate() != null) {
            bugRepository.save(bug);
            if (!bug.getAssignedTo().equals(bug.getCreatedBy()))
                emailSender.sendEmail(userAssignedTo.getEmail(), "New bug added",
                        "Bug added:" + bug.getTitle() + "\n" + bug.getDescription());
            else
                emailSender.sendEmail(userCreatedBy.getEmail(), "New bug added",
                        "Bug added:" + bug.getTitle() + "\n" + bug.getDescription());
            return mapToDTO(bug);
        }
        throw new ValidationException("Bug fields could not be validated");

    }

    @Transactional
    public Boolean removeBug(Long id) {
        if (findBugById(id) == null)
            throw new EntityNotFoundException("Bug could not be found!");
        bugRepository.deleteById(id);
        return true;
    }

    public BugDTO findBugById(Long id) {
        Bug bug = bugRepository.findById(id).orElse(null);
        if (bug == null)
            throw new EntityNotFoundException("Bug could not be found!");
        return mapToDTO(bug);
    }


    public BugDTO findBugByTitle(String title) {
        Bug bug = bugRepository.findByTitle(title);
        if (bug == null)
            throw new EntityNotFoundException("Bug could not be found!");
        return mapToDTO(bug);
    }

    @Transactional
    public BugDTO updateById(Long id, BugDTO updates) throws ValidationException {
        Bug bug=bugRepository.findById(id).orElse(null);


        notificationService.addUserNotificationBugUpdated(bug, updates);
        if (updateIsValid(updates.getTitle()))
            bug.setTitle(updates.getTitle());
        if (updateIsValid(updates.getDescription()))
            bug.setDescription(updates.getDescription());
        if (updateIsValid(updates.getVersion()))
            bug.setVersion(updates.getVersion());
        if (updateIsValid(updates.getFixedVersion()))
            bug.setFixedVersion(updates.getFixedVersion());

        if (updates.getSeverity() == null) {
            throw new ValidationException("Severity can not be empty!");
        }
        bug.setSeverity(updates.getSeverity());

        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User userLogged=userRepository.findByUserName(username);


        //User can't update bug if he's inactive
        if(userLogged.getStatus().equals(UserStatus.INACTIVE) ){
            throw new UserInactiveException("User is inactive ");
        }

        if (bug.getStatus() != updates.getStatus()) {
            switch (bug.getStatus()) {
                case OPEN -> {
                    if (updates.getStatus() == BugStatus.IN_PROGRESS || updates.getStatus() == BugStatus.REJECTED)
                        bug.setStatus(updates.getStatus());
                }
                case REJECTED -> {
                    if (updates.getStatus() == BugStatus.CLOSED && permissionService.hasPermission(userLogged, "BUG_CLOSE"))
                        bug.setStatus(updates.getStatus());
                    else throw new PermissionNotFoundException("You don't have permission to close bugs.");
                }
                case FIXED -> {
                    if (updates.getStatus() == BugStatus.OPEN)
                        bug.setStatus(updates.getStatus());
                    else if (permissionService.hasPermission(userLogged, "BUG_CLOSE") && updates.getStatus() == BugStatus.CLOSED)
                        bug.setStatus(updates.getStatus());
                    else throw new PermissionNotFoundException("You don't have permission to close bugs.");
                }
                case IN_PROGRESS -> {
                    if (updates.getStatus() == BugStatus.REJECTED || updates.getStatus() == BugStatus.INFO_NEEDED || updates.getStatus() == BugStatus.FIXED)
                        bug.setStatus(updates.getStatus());
                }
                case INFO_NEEDED -> {
                    if (updates.getStatus() == BugStatus.IN_PROGRESS)
                        bug.setStatus(updates.getStatus());
                }
            }
        }
        if(updates.getAssignedTo() == null){
            throw new AssignedUserNullException("The assigned user was empty.");
        }
        User user = userRepository.findByUserName(updates.getAssignedTo());
        bugRepository.save(bug);

        if (user != null) {
            emailSender.sendEmail(user.getEmail(), "Bug Update", "Bug:"
                    + bug.getTitle() + "\n" + bug.getDescription());
        }
        return mapToDTO(bug);
    }

    private boolean updateIsValid(String update) {
        if (update == null)
            return false;
        if (update.isEmpty()) {
            return false;
        }
        return !update.isBlank();
    }

    public List<BugDTO> findBugsAssignedTo(String username) {
        return bugRepository.findBugsAssignedTo(username).stream().
                map(this::mapToDTO).toList();
    }

    public List<BugDTO> findBugsCreatedBy(String username) {
        return bugRepository.findBugsCreatedBy(username).stream().
                map(this::mapToDTO).toList();
    }

    public Long getIdByTitle(String title) {
        Bug bug = bugRepository.findByTitle(title);
        return bug.getIdBug();
    }

    public EnumMap<BugStatus, Long> generateStatistics() {
        EnumMap<BugStatus, Long> map = findAll().stream().collect(Collectors.groupingBy(
                BugDTO::getStatus, ()->new EnumMap<>(BugStatus.class), Collectors.counting()));
        EnumSet.allOf(BugStatus.class).forEach(c->map.putIfAbsent(c, 0L));
        return map;
    }

    public Map<String, Long> generateStatisticsCreatedRejected() {
        Map<String, Long> map=new HashMap<>();
        map.put("TOTAL", (long) findAll().size());
        map.put("REJECTED", findAll().stream().filter(bug->bug.getStatus()==BugStatus.REJECTED).count());
        map.put("ACTIVE", map.get("TOTAL")-map.get("REJECTED"));
        return map;
    }

    public Map<String, Long> generateStatisticsForUser(Long id) {
        User user=userRepository.findById(id).orElse(null);
        if(user==null)
            throw new EntityNotFoundException("User not found");
        Map<String, Long> map=new HashMap<>();
        map.put("Created by", (long) findBugsCreatedBy(user.getUsername()).size());
        map.put("Assigned to", (long) findBugsAssignedTo(user.getUsername()).size());
        map.put("Fixed or closed", findBugsAssignedTo(user.getUsername()).stream().filter(bug->bug.getStatus()==BugStatus.FIXED || bug.getStatus()==BugStatus.CLOSED).count());
        return map;
    }
}
