package com.example.booknest.user.infraestructure;

import com.example.booknest.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(TRIM(u.nickname)) = LOWER(TRIM(:nickname))")
    Optional<User> findByNicknameIgnoreCase(@Param("nickname") String nickname);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
