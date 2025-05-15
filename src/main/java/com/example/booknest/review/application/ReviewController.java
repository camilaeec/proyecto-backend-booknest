package com.example.booknest.review.application;

import com.example.booknest.review.domain.ReviewService;
import com.example.booknest.review.dto.ReviewRequestDTO;
import com.example.booknest.review.dto.ReviewResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /*
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO createdReview = reviewService.createReview(request);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }
     */

    /*
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByReviewedUser(@PathVariable Long userId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByUsuarioCalificado(userId);
        return ResponseEntity.ok(reviews);
    }
     */

//    @GetMapping("/{id}")
//    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
//        ReviewResponseDTO review = reviewService.getReviewById(id);
//        return ResponseEntity.ok(review);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteReview(@PathVariable Long id) {
//        reviewService.deleteReview(id);
//    }
}