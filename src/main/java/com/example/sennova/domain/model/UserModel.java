package com.example.sennova.domain.model;

import lombok.Data;


@Data
public class UserModel {
    private Long userId;
    private String name;
    private String email;
    private Long phoneNumber;
    private Long dni;
    private String imageProfile;
    private String position;
    private String refreshToken;
    private String username;
    private Boolean available;
    private String password;
    private RoleModel role;
}
