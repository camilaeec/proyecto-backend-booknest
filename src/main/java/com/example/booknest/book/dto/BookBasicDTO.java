package com.example.booknest.book.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookBasicDTO {
    public String title;
    public List<String> authors;
    public List<String> tags;
    public Double price;
    public List<String> bookPhotos;
}
