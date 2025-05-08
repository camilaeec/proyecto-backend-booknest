package com.example.booknest.events.TransactionOffer;

import com.example.booknest.book.domain.Book;
import com.example.booknest.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransactionOfferEvent extends ApplicationEvent {
    private final User recipient;
    private final User offerer;
    private final Book book;

    public TransactionOfferEvent(Object source, User recipient, User offerer, Book book) {
        super(source);
        this.recipient = recipient;
        this.offerer = offerer;
        this.book = book;
    }
}
