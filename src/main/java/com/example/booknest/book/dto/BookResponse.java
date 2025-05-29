package com.example.booknest.book.dto;

import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookResponse {
    private Long idBook;
    private String title;
    private List<String> authors;
    private List<String> tags;
    private String publisher;
    private String yearOfPublication;
    private String state;
    private List<String> bookPhotos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponseForOtherUsersDTO owner;
}