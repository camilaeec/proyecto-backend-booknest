package com.example.booknest.review.domain;

import com.example.booknest.review.dto.ReviewRequestDTO;
import com.example.booknest.review.dto.ReviewResponseDTO;
import com.example.booknest.review.infraestructure.ReviewRepository;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.domain.UserService;
import com.example.booknest.user.infraestructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        // Obtener usuarios
        User reviewer = userRepository.findById(request.getReviewerUserId())
                .orElseThrow(() -> new RuntimeException("Revisor no encontrado"));

        User reviewed = userRepository.findById(request.getReviewedUserId())
                .orElseThrow(() -> new RuntimeException("Usuario calificado no encontrado"));


        // Validar que no se deje una review a uno mismo
        if (reviewer.getId().equals(reviewed.getId())) {
            throw new RuntimeException("No puedes dejar una reseña a ti mismo");
        }

        // Mapear y setear relaciones
        Review review = modelMapper.map(request, Review.class);
        review.setReviewerUser(reviewer);
        review.setReviewedUser(reviewed);
        review.setReviewDate(LocalDate.now());

        // Guardar
        Review savedReview = reviewRepository.save(review);

        return modelMapper.map(savedReview, ReviewResponseDTO.class);
    }

    public ReviewResponseDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        return modelMapper.map(review, ReviewResponseDTO.class);
    }

    public List<ReviewResponseDTO> getReviewsByReviewedUser(Long idUsuarioCalificado) {
        return reviewRepository.findByReviewedUserId(idUsuarioCalificado).stream()
                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
                .toList();
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada");
        }
        reviewRepository.deleteById(id);
    }
}
