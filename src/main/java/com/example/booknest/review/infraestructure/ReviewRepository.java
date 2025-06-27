package com.example.booknest.review.infraestructure;

import com.example.booknest.review.domain.Review;
import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN r.reviewed u WHERE u.nickname = :nickname")
    List<Review> findByReviewedUserNickname(@Param("nickname") String nickname);
    boolean existsByReviewerAndTransaction(User reviewer, Transaction transaction);
}
