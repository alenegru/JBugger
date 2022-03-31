package com.example.springbootdemo.model;

import com.example.springbootdemo.controller.BugController;
import com.example.springbootdemo.controller.UserController;
import com.example.springbootdemo.dto.UserDTO;
import com.example.springbootdemo.model.enums.UserStatus;
import lombok.*;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_gen")
    @SequenceGenerator(name = "users_gen", sequenceName = "users_seq", allocationSize = 1)
    private Long idUser;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Pattern(regexp = "\\(?\\+\\(?49\\)?[ ()]?([- ()]?\\d[- ()]?){10}|\\(?\\+\\(?40\\)?[ ()]?([- ()]?\\d[- ()]?){9}")
    private String mobileNumber;

    @Column(nullable = false)
    @Pattern(regexp="^[A-Za-z0-9.-]+@msg\\.group$")
    private String email;

    @Column(unique = true)
    private String username;

    private String password;
    private UserStatus status;

    @OneToMany(mappedBy = "createdBy")
    private List<Bug> createdBugs;

    @OneToMany(mappedBy = "assignedTo")
    private List<Bug> assignedFor;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable( name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id_user"),
            inverseJoinColumns = @JoinColumn(name = "role_id_role"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<UserNotification> userNotificationList;

    @Column(columnDefinition = "integer default 0")
    private Short failed;

    public static UserDTO mapToDTO(User user){
        UserDTO dto=new UserDTO();
        dto.setIdUser(user.getIdUser());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setStatus(user.getStatus());

        if(user.getRoles()!=null)
            dto.setRoles(user.getRoles().stream().map(Role::mapToDTO).toList());
        dto.add(linkTo(UserController.class).slash(user.getIdUser()).withSelfRel());
        dto.add(linkTo(methodOn(BugController.class).findBugsAssignedTo(dto.getUsername())).withRel("assignedTo"));
        dto.add(linkTo(methodOn(BugController.class).findBugsCreatedBy(dto.getUsername())).withRel("createdBy"));
        return dto;
    }

}
