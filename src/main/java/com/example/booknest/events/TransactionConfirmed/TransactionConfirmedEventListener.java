package com.example.booknest.events.TransactionConfirmed;

import com.example.booknest.email.domain.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConfirmedEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handleTransactionConfirmedEvent(TransactionConfirmedEvent event) throws MessagingException {
        emailService.sendTransactionConfirmed(
                event.getUser().getEmail(),
                event.getUser().getUsername(),
                event.getBook().getTitle()
        );
    }
}
