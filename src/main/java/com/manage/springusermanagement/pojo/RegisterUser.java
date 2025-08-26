package com.manage.springusermanagement.pojo;


import com.manage.springusermanagement.entity.Role;
import lombok.Data;

@Data
public class RegisterUser {
    private  String username;
    private String password;
    private Role role;

}
