package com.manage.springusermanagement.service;

import com.manage.springusermanagement.entity.Role;
import com.manage.springusermanagement.entity.Users;
import com.manage.springusermanagement.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordInitializer {
    @Bean

//    as a ideal case we are keeping one admin to create other admin
    public CommandLineRunner init(UserRepo  userRepo, PasswordEncoder passwordEncoder) {
        return  args ->{
            if(userRepo.findByUsername("admin").isEmpty()){
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("passis@123"));
                admin.setRole(Role.ADMIN);
                userRepo.save(admin);

                System.out.println("Admin users is created.");
            }

        };
    }
}
