package com.example.springbootdemo.dto;

import com.example.springbootdemo.model.User;
import com.example.springbootdemo.model.enums.UserStatus;
import lombok.*;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@Data
public class UserDTO extends RepresentationModel<UserDTO> {
    private Long idUser;
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @Column(name = "mobile_number")
    private String mobileNumber;

    private String password;
    private String username;

    private String token;
    private UserStatus status;

    private List<RoleDTO> roles;


}
