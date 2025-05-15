package com.example.booknest.events.SignIn;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignInEvent extends ApplicationEvent {
    private final String username;
    private final String email;

    public SignInEvent(Object source,String email,String username) {
        super(source);
        this.email = email;
        this.username = username;
    }
}
