package com.example.booknest.config;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.dto.BookBasicDTO;
import com.example.booknest.review.domain.Review;
import com.example.booknest.review.dto.ReviewResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setSkipNullEnabled(true);

        modelMapper.createTypeMap(BookBasicDTO.class, Book.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getAuthors() != null ? src.getAuthors() : new ArrayList<>(), Book::setAuthors);
                    mapper.map(src -> src.getTags() != null ? src.getTags() : new ArrayList<>(), Book::setTags);
                    mapper.map(src -> src.getBookPhotos() != null ? src.getBookPhotos() : new ArrayList<>(), Book::setBookPhotos);
                });

        /*
        modelMapper.createTypeMap(Review.class, ReviewResponseDTO.class)
                .addMapping(src -> src.getReviewerUser(), ReviewResponseDTO::setReviewerUser)
                .addMapping(src -> src.getReviewedUser(), ReviewResponseDTO::setReviewedUser);
         */

        return modelMapper;
    }
}
