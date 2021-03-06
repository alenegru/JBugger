package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.UserDTO;
import com.example.springbootdemo.email.EmailSender;
import com.example.springbootdemo.model.Role;
import com.example.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Document me.
 *
 * @author msg systems AG; User Name.
 * @since 19.1.2
 */
@RestController
@CrossOrigin(origins = "*", originPatterns = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSender emailSender;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/findRoles/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Role>> findAllRoles(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findAllRoles(id), HttpStatus.OK);
    }

    @PostMapping("/users/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user) throws ValidationException {
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @PostMapping("/users/add")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) throws Exception {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/users/delete/{id}")
    public Boolean deleteUserById(@PathVariable Long id) {
        return userService.removeUser(id);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @GetMapping("/users/find/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }


    @PostMapping("/users/addRole/{id}/{id2}")
    public Boolean addRoleUser(@PathVariable Long id, @PathVariable Long id2) {
        return userService.addRoleById(id, id2);
    }

    @PostMapping("/users/email")
    public Boolean sendEmail(@RequestParam String email, @RequestParam String subject,
                             @RequestParam String text) {
        return emailSender.sendEmail(email, subject, text);
    }

    @PutMapping("/users/activate/{id}")
    public UserDTO activateUser(@PathVariable Long id) {
        return userService.activateUser(id);
    }

    @PutMapping("/users/deactivate/{id}")
    public UserDTO deactivateUser(@PathVariable Long id) {
        return userService.deactivateUser(id);
    }
}
