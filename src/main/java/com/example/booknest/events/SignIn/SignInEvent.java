package com.example.booknest.events.SignIn;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignInEvent extends ApplicationEvent {
    private final String email;
    private final String username;

    public SignInEvent(String email, String username) {
        super(email);
        this.email = email;
        this.username = username;
    }
}
