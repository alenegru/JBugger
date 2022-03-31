package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.RoleDTO;
import com.example.springbootdemo.dto.UserDTO;
import com.example.springbootdemo.email.EmailSender;
import com.example.springbootdemo.error.exceptions.UnclosedBugsException;
import com.example.springbootdemo.model.Bug;
import com.example.springbootdemo.model.Role;
import com.example.springbootdemo.model.User;
import com.example.springbootdemo.model.enums.BugStatus;
import com.example.springbootdemo.model.enums.UserStatus;
import com.example.springbootdemo.repository.BugRepository;
import com.example.springbootdemo.repository.RoleRepository;
import com.example.springbootdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.example.springbootdemo.model.User.mapToDTO;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BugRepository bugRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private NotificationService notificationService;

    public List<UserDTO> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(User::mapToDTO).toList();
    }


    public List<Role> findAllRoles(Long id){
        User user=userRepository.findById(id).orElse(null);
        List<Role> roles= new ArrayList<>();
        assert user != null;
        if (!user.getRoles().isEmpty())
            roles.addAll(user.getRoles());
        return roles;
    }

    @Transactional
    public UserDTO update(UserDTO userWithUpdates) throws ValidationException {
        String loggedInUser= SecurityContextHolder.getContext().getAuthentication().getName();

        User userFromRepo=userRepository.findByUserName(userWithUpdates.getUsername());
        User userOld=new User();

        userOld.setFirstName(userFromRepo.getFirstName());
        userOld.setLastName(userFromRepo.getLastName());
        userOld.setMobileNumber(userFromRepo.getMobileNumber());
        userOld.setEmail(userFromRepo.getEmail());

        if (validateEmailAndMobile(userWithUpdates.getEmail(), userWithUpdates.getMobileNumber())
                && !userWithUpdates.getFirstName().equals("")
                && !userWithUpdates.getLastName().equals("")) {
            userFromRepo.setFirstName(userWithUpdates.getFirstName());
            userFromRepo.setLastName(userWithUpdates.getLastName());
            userFromRepo.setEmail(userWithUpdates.getEmail());
            userFromRepo.setMobileNumber(userWithUpdates.getMobileNumber());
        } else {
            throw new ValidationException("One of the fields was not valid");
        }

        if(notificationService.addUserNotificationUserUpdated(userFromRepo, userOld, loggedInUser)==null)
            return null;

        userRepository.save(userFromRepo);
        addMultipleRoles(userFromRepo.getUsername(), userWithUpdates.getRoles());
        return mapToDTO(userFromRepo);
    }

    @Transactional
    public UserDTO addUser(UserDTO user) throws ValidationException {

        User userSimple = mapToUser(user);
        userSimple.setRoles(new HashSet<>());
        if (!userSimple.getFirstName().equals("") && !userSimple.getLastName().equals("")
                && !userSimple.getUsername().equals("") && !userSimple.getPassword().equals("")
                && !userSimple.getMobileNumber().equals("") &&
                validateEmailAndMobile(userSimple.getEmail(), userSimple.getMobileNumber())) {

            String passNotEncrypted = userSimple.getPassword();
            userSimple.setStatus(UserStatus.INACTIVE);
            //userSimple.setPassword(encoder.encode(userSimple.getPassword()));
            System.out.println(userSimple.getPassword());
            userRepository.save(userSimple);
            addMultipleRoles(userSimple.getUsername(), user.getRoles());
            String text = "Name: " + userSimple.getFirstName() + " " + userSimple.getLastName() + "\n" + "Username: " + userSimple.getUsername() + "\n" + "Password: " + passNotEncrypted;

            notificationService.addUserNotificationWelcomeNewUser(userSimple);
            emailSender.sendEmail(userSimple.getEmail(), "Successful registration", text);
            return mapToDTO(userSimple);
        } else {
            throw new ValidationException("One of the fields was not valid!");

        }
    }

    private boolean validateEmailAndMobile(String mail, String mobile) {
        String mailRegex = "^[A-Za-z0-9.-]+@msg\\.group$";
        String mobileRegex = "\\(?\\+\\(?49\\)?[ ()]?([- ()]?\\d[- ()]?){10}|\\(?\\+\\(?40\\)?[ ()]?([- ()]?\\d[- ()]?){9}";
        return mail != null && mobile != null && !mail.equals("") && !mobile.equals("") && mail.matches(mailRegex) && mobile.matches(mobileRegex);
    }

    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException("User could not be found!");

        return mapToDTO(user);
    }

    public UserDTO findUserByUsername(String username) {
        User user = userRepository.findByUserName(username);
        if (user == null)
            throw new UsernameNotFoundException("User " + username + " could not be found!");

        return mapToDTO(user);
    }


    private List<String> getUserNamesLike(String toString) {
        return userRepository.getUserNamesLike(toString);
    }


    @Transactional
    public Boolean removeUser(Long id) {
        if (findUserById(id) == null)
            throw new UsernameNotFoundException("User could not be found!");

        userRepository.deleteById(id);
        return true;
    }




    @Transactional
    public Boolean deleteRole(Long idUser, Long idRole) {
        User user = userRepository.findById(idUser).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException("User could not be found!");

        Role role = roleRepository.findById(idRole).orElse(null);
        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");

        user.getRoles().remove(role);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public Boolean addRoleById(Long idUser, Long idRole) {
        Role role = roleRepository.findById(idRole).orElse(null);
        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");
        User user = userRepository.findById(idUser).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException("User could not be found!");

        user.getRoles().add(role);
        userRepository.save(user);
        return true;

    }

    @Transactional
    public void addRoleByUsername(String username, String roleType) {
        Role role = roleRepository.findByType(roleType);
        if (role == null)
            throw new EntityNotFoundException("Role could not be found!");

        User user = userRepository.findByUserName(username);
        if (user == null)
            throw new UsernameNotFoundException("User could not be found!");

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public UserDTO activateUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException("User could not be found!");
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return mapToDTO(user);
    }

    @Transactional
    public UserDTO deactivateUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException("User could not be found!");
        List<Bug> bugs = bugRepository.returnAllAssignedTo(user);
        if (bugs.size() > 0 && bugs.stream().filter(b -> b.getStatus() == BugStatus.OPEN).toList().size() > 0)
            throw new UnclosedBugsException("User has unclosed bugs");

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        notificationService.addUserNotificationUserDeactivated(user);
        return mapToDTO(user);
    }

    @Transactional
    public void addMultipleRoles(String username, List<RoleDTO> roles) {
        if (roles == null)
            return;
        User user = userRepository.findByUserName(username);
        user.setRoles(new HashSet<>());
        for (RoleDTO role : roles) {
            addRoleByUsername(username, role.getType());
        }
    }

    public User mapToUser(UserDTO userDTO) {
        User user=new User();
        user.setEmail(userDTO.getEmail());
        if (userDTO.getIdUser() != null) {
            user.setIdUser(userDTO.getIdUser());
        }
        if (userDTO.getUsername() != null && userDTO.getPassword() != null) {
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
        } else {
            user.setUsername(generateUserName(userDTO.getFirstName(), userDTO.getLastName()));
            user.setPassword(user.getUsername());
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setStatus(userDTO.getStatus());
        user.setMobileNumber(userDTO.getMobileNumber());
        if (userDTO.getRoles() != null)
            user.setRoles(userDTO.getRoles().stream().map(Role::mapToRole).collect(Collectors.toSet()));
        return user;
    }

    private String generateUserName(String firstName, String lastName) {
        firstName = firstName.toLowerCase(Locale.ROOT);
        lastName = lastName.toLowerCase(Locale.ROOT);
        StringBuilder stringBuilder = new StringBuilder();
        if (firstName.length() >= 5) {
            stringBuilder.append(firstName, 0, 5);
            stringBuilder.append(lastName.charAt(0));
        } else {
            stringBuilder.append(firstName);
            if (lastName.length() >= 6 - firstName.length())
                stringBuilder.append(lastName, 0, 6 - firstName.length());
            else {
                stringBuilder.append(lastName);
                if (stringBuilder.toString().length() < 6) {
                    stringBuilder.append("ABCD", 0, 6 - stringBuilder.toString().length());
                }
            }
        }
        List<String> usernamesLike = getUserNamesLike(stringBuilder.toString());
        if (usernamesLike.contains(stringBuilder.toString())) {
            stringBuilder.append(usernamesLike.size() + 1);
        }
        return stringBuilder.toString();
    }
}

