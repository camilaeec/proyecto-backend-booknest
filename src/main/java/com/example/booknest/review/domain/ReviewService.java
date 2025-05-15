package com.example.booknest.review.domain;

import com.example.booknest.review.dto.ReviewRequestDTO;
import com.example.booknest.review.dto.ReviewResponseDTO;
import com.example.booknest.review.infraestructure.ReviewRepository;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /*
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        User reviewer = userService.getById(request.getReviewerUserId())
                .orElseThrow(() -> new RuntimeException("Revisor no encontrado"));
        User reviewed = userService.getById(request.getReviewedUserId())
                .orElseThrow(() -> new RuntimeException("Usuario calificado no encontrado"));

        Review = modelMapper.map(request, Review.class);
        review.setReviewerUser(reviewer);
        review.setReviewedUser(reviewed);
        review.setReviewDate(LocalDate.now());

        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewResponseDTO.class);
    }
    */

    // Obtener reseña por ID
    public ReviewResponseDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        return modelMapper.map(review, ReviewResponseDTO.class);
    }
//
//    // Obtener reseñas por usuario calificado (corregido)
//    public List<ReviewResponseDTO> getReviewsByReviewedUser(Long idUsuarioCalificado) {
//        return reviewRepository.findByReviewedUserIdUser(idUsuarioCalificado).stream() // ✔️ Updated
//                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
//                .toList();
//    }
//
//    public void deleteReview(Long id) {
//        if (!reviewRepository.existsById(id)) {
//            throw new RuntimeException("Reseña no encontrada");
//        }
//        reviewRepository.deleteById(id);
//    }
}