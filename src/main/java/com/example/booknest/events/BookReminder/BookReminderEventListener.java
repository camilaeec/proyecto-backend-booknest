package com.example.booknest.events.BookReminder;

import com.example.booknest.email.domain.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookReminderEventListener {

    private final EmailService emailService;

    @EventListener
    @Async
    public void handleBookReminderEvent(BookReminderEvent event) throws MessagingException {
        String email = event.getUser().getEmail();
        String title = event.getBook().getTitle();
        emailService.sendBookReminder(email, title);
    }
}
