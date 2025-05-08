package com.example.booknest.events.BookReminder;

import com.example.booknest.book.domain.Book;
import com.example.booknest.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookReminderEvent extends ApplicationEvent {
    private final User user;
    private final Book book;


    public BookReminderEvent(Object source, User user, Book book) {
        super(source);
        this.user = user;
        this.book = book;
    }
}
