package com.manage.springusermanagement.service;

import com.manage.springusermanagement.entity.Role;
import com.manage.springusermanagement.entity.Users;
import com.manage.springusermanagement.pojo.RegisterUser;
import com.manage.springusermanagement.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserRegisterService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserRegisterService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String registerNewUserRequest(RegisterUser user){
//        TODO add new userExisting if doesn't exist :DONE
        Optional<Users> userExisting = repo.findByUsername(user.getUsername());
        if(userExisting.isPresent()){
            return "User already exists";
        }

        //        TODO : encrypt the password :DONE
        Users newSaveUser = new Users();
        newSaveUser.setUsername(user.getUsername());
        newSaveUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newSaveUser.setRole(user.getRole());

        //        TODO : save the userExisting :DONE
        Users savedUser = repo.save(newSaveUser);
        log.info("New User is created  {}",savedUser);

        return "User registered successfully";
    }

}
