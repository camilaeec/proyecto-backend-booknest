package com.example.booknest.review.infraestructure;

import com.example.booknest.review.domain.Review;
import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewedUserNickname(String nickname);
    boolean existsByReviewerUserAndTransaction(User reviewer, Transaction transaction);
}
