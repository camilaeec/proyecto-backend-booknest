package com.example.booknest.events.TransactionOffer;

import com.example.booknest.email.domain.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionOfferEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handleTransactionOfferEvent(TransactionOfferEvent event) throws MessagingException {
        emailService.sendTransactionOffer(
                event.getRecipient().getEmail(),
                event.getBook().getTitle(),
                event.getOfferer().getUsername()
        );
    }
}
