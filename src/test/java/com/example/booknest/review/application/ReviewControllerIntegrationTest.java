package com.example.booknest.review.application;

import com.example.booknest.review.domain.ReviewService;
import com.example.booknest.review.dto.ReviewRequestDTO;
import com.example.booknest.review.dto.ReviewResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ReviewRequestDTO reviewRequest;
    private ReviewResponseDTO reviewResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

        reviewRequest = new ReviewRequestDTO();
        reviewRequest.setReviewerNickname("usuario1");
        reviewRequest.setReviewedNickname("usuario2");
        reviewRequest.setTransactionId(1L);
        reviewRequest.setRating(5);
        reviewRequest.setComment("Excelente servicio");

        reviewResponse = new ReviewResponseDTO();
        reviewResponse.setIdReview(1L);
        reviewResponse.setReviewerNickname("usuario1");
        reviewResponse.setReviewedNickname("usuario2");
        reviewResponse.setTransactionId(1L);
        reviewResponse.setRating(5);
        reviewResponse.setComment("Excelente servicio");
        reviewResponse.setReviewDate(LocalDate.now());
    }

    @Test
    void createReview_ValidRequest_ReturnsCreated() throws Exception {
        when(reviewService.createReview(any(ReviewRequestDTO.class))).thenReturn(reviewResponse);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReview").value(1L))
                .andExpect(jsonPath("$.reviewerNickname").value("usuario1"))
                .andExpect(jsonPath("$.reviewedNickname").value("usuario2"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void createReview_InvalidRequest_ReturnsBadRequest() throws Exception {
        reviewRequest.setRating(6); // Rating inválido

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsByUser_ValidNickname_ReturnsOk() throws Exception {
        List<ReviewResponseDTO> reviews = Collections.singletonList(reviewResponse);
        when(reviewService.getReviewsByUser(anyString())).thenReturn(reviews);

        mockMvc.perform(get("/reviews/user/usuario2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReview").value(1L))
                .andExpect(jsonPath("$[0].reviewerNickname").value("usuario1"))
                .andExpect(jsonPath("$[0].reviewedNickname").value("usuario2"));
    }

    @Test
    void getReviewById_ValidId_ReturnsOk() throws Exception {
        when(reviewService.getReviewById(anyLong())).thenReturn(reviewResponse);

        mockMvc.perform(get("/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReview").value(1L))
                .andExpect(jsonPath("$.reviewerNickname").value("usuario1"))
                .andExpect(jsonPath("$.reviewedNickname").value("usuario2"));
    }

    @Test
    void deleteReview_ReturnsNoContent() throws Exception {
        doNothing().when(reviewService).deleteReview(anyLong());

        mockMvc.perform(delete("/reviews/1"))
                .andExpect(status().isNoContent());
    }
}