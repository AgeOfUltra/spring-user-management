package com.manage.springusermanagement.controller;

import com.manage.springusermanagement.entity.Role;
import com.manage.springusermanagement.entity.Users;
import com.manage.springusermanagement.pojo.RegisterUser;
import com.manage.springusermanagement.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRegisterController {
    @Autowired
    UserRegisterService userRegisterService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUser newUser) {
        String result = userRegisterService.registerNewUserRequest(newUser);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterUser newAdmin) {
        String result = userRegisterService.registerNewUserRequest(newAdmin);
        return ResponseEntity.ok(result);
    }

}
