package com.example.booknest.user.application;

import com.example.booknest.user.domain.User;
import com.example.booknest.user.infraestructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    ResponseEntity<User> getUser(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);
        return ResponseEntity.ok(user.orElse(null));
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User request) {
        return ResponseEntity.ok(userRepository.save(request));
    }

    @PatchMapping
    ResponseEntity<User> updateUser(@RequestBody User request) {
        return ResponseEntity.ok(userRepository.save(request)); //modificar
    }

    @DeleteMapping("/{id}")
    ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }
}
