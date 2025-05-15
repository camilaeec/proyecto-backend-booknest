package com.example.booknest.Auth.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String name;
    private String lastname;
    private String nickname;
    private String email;
    private String password;
    private String location;
    private String phoneNumber;
}
