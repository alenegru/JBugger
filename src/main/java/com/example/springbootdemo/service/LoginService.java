package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.UserDTO;
import com.example.springbootdemo.error.exceptions.TooManyFailedAttemptException;
import com.example.springbootdemo.error.exceptions.UserInactiveException;
import com.example.springbootdemo.error.exceptions.UserPasswordIncorrectException;
import com.example.springbootdemo.model.Permission;
import com.example.springbootdemo.model.Role;
import com.example.springbootdemo.model.User;
import com.example.springbootdemo.model.enums.UserStatus;
import com.example.springbootdemo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public UserDTO login(String username, String pwd) throws Exception {
        User user = validateAndGet(username, pwd);
        if (user == null)
            throw new UserPasswordIncorrectException("User or password incorrect!");
        if(user.getStatus()!= UserStatus.ACTIVE)
            throw new UserInactiveException("User is inactive!");
        String token = getJWTToken(username, user.getIdUser());
        UserDTO dto = User.mapToDTO(user);
        dto.setToken(token);

        return dto;
    }

    public User validateAndGet(String username, String pwd) {
        User user = userRepository.findByUserName(username);
//        if (user == null)
//            return null;
//        if (encoder.matches(pwd, user.getPassword())){
//            userRepository.resetFailed(username);
//            return user;
//        }
//        userRepository.increaseFailed(username);
//        if(userRepository.getFailed(username)==5){
//            userRepository.resetFailed(username);
//            userRepository.forcedDeactivate(username);
//            throw new TooManyFailedAttemptException("User deactivated for too many failed attempts!");
//        }
        return user;
    }

    private String getJWTToken(String username, Long id) {
        String secretKey = "mySecretKey";
        User user = userRepository.findById(id).get();
        Set<String> permissions = new HashSet<>();
        for (Role role : user.getRoles()) {
            for (Permission perm : role.getPermissions()) {
                permissions.add(perm.getType());
            }

        }
        String auth = String.join(",", permissions);
        List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(auth);
        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorityList.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        return token;
    }
}
