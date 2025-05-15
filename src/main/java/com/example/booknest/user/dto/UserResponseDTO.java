package com.example.booknest.user.dto;

import lombok.Data;

@Data
public class UserResponseDTO { //Obtener mi info
    private Long idUser;
    private String nickname;
    private String name;
    private String lastname;
    private String email;
    private String phoneNumber;
}
