package com.example.booknest.book.dto;

import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookResponseDtoValidationTest {

    @Test
    void testBookResponseStructure() {
        BookResponse response = new BookResponse();
        response.setIdBook(1L);
        response.setTitle("Effective Java");
        response.setAuthors(List.of("Joshua Bloch"));
        response.setTags(List.of("Programming"));
        response.setPublisher("Addison-Wesley");
        response.setYearOfPublication("2008");
        response.setState("Nuevo");
        response.setBookPhotos(List.of("photo1.jpg"));
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        UserResponseForOtherUsersDTO owner = new UserResponseForOtherUsersDTO();
        owner.setNickname("booklover");
        response.setOwner(owner);

        assertNotNull(response);
        assertEquals(1L, response.getIdBook());
        assertEquals("Effective Java", response.getTitle());
        assertEquals(1, response.getAuthors().size());
        assertEquals("Joshua Bloch", response.getAuthors().get(0));
        assertNotNull(response.getOwner());
        assertEquals("booklover", response.getOwner().getNickname());
    }
}