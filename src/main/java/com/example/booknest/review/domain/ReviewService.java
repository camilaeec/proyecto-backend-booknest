package com.example.booknest.review.domain;

import com.example.booknest.exception.ResourceNotFoundException;
import com.example.booknest.review.dto.ReviewRequestDTO;
import com.example.booknest.review.dto.ReviewResponseDTO;
import com.example.booknest.review.infraestructure.ReviewRepository;
import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.transaction.infraestructure.TransactionRepository;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.infraestructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        String reviewerNick = request.getReviewerNickname().trim().toLowerCase();
        String reviewedNick = request.getReviewedNickname().trim().toLowerCase();
        User reviewer = userRepository.findByNicknameIgnoreCase(reviewerNick)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer no encontrado"));

        User reviewed = userRepository.findByNicknameIgnoreCase(reviewedNick)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario reseñado no encontrado"));

        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Transacción no encontrada"));

        if (!transaction.getBuyer().equals(reviewer) && !transaction.getSeller().equals(reviewer)) {
            throw new IllegalStateException("El revisor no participó en esta transacción");
        }

        if (!transaction.getBuyer().equals(reviewed) && !transaction.getSeller().equals(reviewed)) {
            throw new IllegalStateException("El usuario reseñado no participó en esta transacción");
        }

        if (reviewer.equals(reviewed)) {
            throw new IllegalStateException("No puedes reseñarte a ti mismo");
        }

        if (reviewRepository.existsByReviewerAndTransaction(reviewer, transaction)) {
            throw new IllegalStateException("Ya has reseñado esta transacción");
        }

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setReviewer(reviewer);
        review.setReviewed(reviewed);
        review.setTransaction(transaction);

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    public ReviewResponseDTO convertToDTO(Review review) {
        return modelMapper.map(review, ReviewResponseDTO.class);
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada");
        }
        reviewRepository.deleteById(id);
    }

    public List<ReviewResponseDTO> getReviewsByUser(String nickname) {
        return reviewRepository.findByReviewedUserNickname(nickname)
                .stream().map(this::convertToDTO).toList();
    }

    public ReviewResponseDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada"));
        return convertToDTO(review);
    }
}