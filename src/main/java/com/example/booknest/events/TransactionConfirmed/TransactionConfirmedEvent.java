package com.example.booknest.events.TransactionConfirmed;

import com.example.booknest.book.domain.Book;
import com.example.booknest.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransactionConfirmedEvent extends ApplicationEvent {
    private final User user;
    private final Book book;

    public TransactionConfirmedEvent(Object source, User user, Book book) {
        super(source);
        this.user = user;
        this.book = book;
    }
}
