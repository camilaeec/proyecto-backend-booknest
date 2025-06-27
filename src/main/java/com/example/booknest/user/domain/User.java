package com.example.booknest.user.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.review.domain.Review;
import com.example.booknest.transaction.domain.Transaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private Long id;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="nickname", nullable=false, unique=true)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="phone_number", nullable=false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="date_of_registration")
    private LocalDate dateOfRegistration=LocalDate.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Book> books;

    @OneToMany(mappedBy= "buyer")
    @JsonManagedReference
    private List<Transaction> purchases;

    @OneToMany(mappedBy = "seller")
    @JsonManagedReference
    private List<Transaction> sales;

    @OneToMany(mappedBy = "reviewer")
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "reviewed")
    @JsonManagedReference
    private List<Review> reviewsReceived;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
