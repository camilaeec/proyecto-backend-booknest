package com.example.booknest.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private Long idUser;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String location;

    @Column(name="phonenumber")
    private String phonenumber;

    @Column(name="date_of_registration")
    private LocalDate dateOfRegistration=LocalDate.now();

    }
