package com.example.booknest.Auth.application;

import com.example.booknest.Auth.domain.AuthService;
import com.example.booknest.Auth.dto.JwtAuthResponse;
import com.example.booknest.Auth.dto.LoginDTO;
import com.example.booknest.Auth.dto.RegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> register(@RequestBody RegisterDTO registerDTO){
        return ResponseEntity.ok(authService.registerCommonUser(registerDTO));
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<JwtAuthResponse> registerAdmin(@RequestBody RegisterDTO registerDTO){
        return ResponseEntity.ok(authService.registerAdminUser(registerDTO));
    }
}
