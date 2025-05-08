package com.example.booknest.events.SignIn;

import com.example.booknest.email.domain.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInEventListener {

    private final EmailService emailService;

    @EventListener
    @Async
    public void handleSignInEvent(SignInEvent event) throws MessagingException {
        emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
    }
}
