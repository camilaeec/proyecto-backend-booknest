package com.example.booknest.user.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.infraestructure.BookRepository;
import com.example.booknest.exception.ResourceAlreadyExists;
import com.example.booknest.exception.ResourceNotFoundException;
import com.example.booknest.exception.UserMustBeAuthenticatedException;
import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import com.example.booknest.user.dto.UserRequestDTO;
import com.example.booknest.user.dto.UserResponseDTO;
import com.example.booknest.user.infraestructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final BookRepository bookRepository;

    private User getByEmail(String email){ //Solo para usos locales!!
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getMeLocal(){ // Solo se usa para funciones dentro del backend
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()){
            throw new UserMustBeAuthenticatedException("User is not authenticated");
        }
        Object principal = auth.getPrincipal();
        if(principal instanceof User user){
            return user;
        }else{
            throw new ResourceNotFoundException("Invalid User");
        }
    }

    public UserResponseDTO getMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()){
            throw new UserMustBeAuthenticatedException("User is not authenticated");
        }
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = getByEmail(email);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseDTO updateMyCommonData(UserRequestDTO userRequestDTO){
        User user = getMeLocal();
        if(userRequestDTO.getEmail() != null){
            boolean user1 = userRepository.existsByEmail(userRequestDTO.getEmail());
            if(user1){
                throw new ResourceAlreadyExists("Email already in user");
            }else{
                user.setEmail(userRequestDTO.getEmail());
            }
        }
        if(userRequestDTO.getNickname() != null){
            boolean user2 = userRepository.existsByNickname(userRequestDTO.getNickname());
            if(user2){
                throw new ResourceAlreadyExists("Nickname already in user");
            }else{
                user.setNickname(userRequestDTO.getNickname());
            }
        }
        if(userRequestDTO.getName() != null){
            user.setName(userRequestDTO.getName());
        }
        if(userRequestDTO.getLastname() != null){
            user.setLastname(userRequestDTO.getLastname());
        }
        if(userRequestDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        if(userRequestDTO.getPhoneNumber() != null){
            user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        }
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseForOtherUsersDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseForOtherUsersDTO.class);
    }

    public UserResponseForOtherUsersDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseForOtherUsersDTO.class);
    }

    public void deleteUserByEmail(String email){
        userRepository.delete(userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    public void deleteUserById(Long id){
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    @Bean(name = "UserDetailsService")
    public UserDetailsService userDetailsService(){
        return username -> {
            User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return (UserDetails) user;
        };
    }

    public List<Book> getBooksByCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return bookRepository.findByUser(user);
    }
}
