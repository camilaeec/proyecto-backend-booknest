package com.example.booknest.review.application;

import com.example.booknest.review.domain.ReviewService;
import com.example.booknest.review.dto.ReviewRequestDTO;
import com.example.booknest.review.dto.ReviewResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('COMMON_USER')")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @Valid @RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO createdReview = reviewService.createReview(request);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/user/{nickname}")
    @PreAuthorize("hasRole('COMMON_USER')")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUser(
            @PathVariable String nickname) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByUser(nickname);
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('COMMON_USER')")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        ReviewResponseDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}