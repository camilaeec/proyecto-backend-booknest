package com.example.booknest.config;

import com.example.booknest.book.domain.Book;
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

        /*
        modelMapper.createTypeMap(Review.class, ReviewResponseDTO.class)
                .addMapping(src -> src.getReviewerUser(), ReviewResponseDTO::setReviewerUser)
                .addMapping(src -> src.getReviewedUser(), ReviewResponseDTO::setReviewedUser);
         */

        return modelMapper;
    }
}
