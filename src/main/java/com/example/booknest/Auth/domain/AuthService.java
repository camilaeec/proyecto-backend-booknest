package com.example.booknest.Auth.domain;

import com.example.booknest.Auth.dto.JwtAuthResponse;
import com.example.booknest.Auth.dto.LoginDTO;
import com.example.booknest.Auth.dto.RegisterDTO;
import com.example.booknest.config.JwtService;
import com.example.booknest.events.SignIn.SignInEvent;
import com.example.booknest.exception.PasswordIncorrectException;
import com.example.booknest.exception.ResourceAlreadyExists;
import com.example.booknest.exception.ResourceNotFoundException;
import com.example.booknest.user.domain.Role;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.infraestructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public JwtAuthResponse login(LoginDTO loginDTO){
        User user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist or incorrect email "));
        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new PasswordIncorrectException("Your password is incorrect");
        }
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(jwtService.generatedToken(user));
        return response;
    }

    public JwtAuthResponse registerCommonUser(RegisterDTO registerDTO){
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new ResourceAlreadyExists("Email already registered");
        }
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        if(userRepository.findByNicknameIgnoreCase(registerDTO.getNickname()).isPresent()){
            throw new ResourceAlreadyExists("User with that nickname already exists");
        }
        user.setNickname(registerDTO.getNickname());
        user.setName(registerDTO.getName());
        user.setLastname(registerDTO.getLastname());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setRole(Role.COMMON_USER);
        userRepository.save(user);
        applicationEventPublisher.publishEvent(new SignInEvent(this, registerDTO.getEmail(), registerDTO.getName()));
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(jwtService.generatedToken(user));
        return response;
    }

    public JwtAuthResponse registerAdminUser(RegisterDTO registerDTO){
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new ResourceAlreadyExists("Email already registered");
        }
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setNickname(registerDTO.getNickname());
        user.setName(registerDTO.getName());
        user.setLastname(registerDTO.getLastname());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        applicationEventPublisher.publishEvent(new SignInEvent(this, registerDTO.getEmail(), registerDTO.getName()));
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(jwtService.generatedToken(user));
        return response;
    }
}
