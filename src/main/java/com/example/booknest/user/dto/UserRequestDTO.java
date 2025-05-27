package com.example.booknest.user.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String email;
    private String nickname;
    private String name;
    private String lastname;
    private String password;
    private String phoneNumber;

//    public UserRequestDTO(String username, String password) {}
}
