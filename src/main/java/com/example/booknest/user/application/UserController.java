package com.example.booknest.user.application;

import com.example.booknest.user.domain.User;
import com.example.booknest.user.domain.UserService;
import com.example.booknest.user.dto.UseResponseForOtherUsersDTO;
import com.example.booknest.user.dto.UserRequestDTO;
import com.example.booknest.user.dto.UserResponseDTO;
import com.example.booknest.user.infraestructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getMe")
    public ResponseEntity<UserResponseDTO> getMe(){
        UserResponseDTO userResponseDTO = userService.getMe();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @PatchMapping("/udpateMe")
    public ResponseEntity<UserResponseDTO> updateMe(@RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = userService.updateMyCommonData(userRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @GetMapping("/getByEmail/email/{email}")
    public ResponseEntity<UseResponseForOtherUsersDTO> getByEmail(@PathVariable String email){
        UseResponseForOtherUsersDTO userResponseDTO = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @GetMapping("/getById/id/{id}")
    public ResponseEntity<UseResponseForOtherUsersDTO> getById(@PathVariable Long id){
        UseResponseForOtherUsersDTO userResponseDTO = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @DeleteMapping("/deleteUser/delete/{email}")
    public ResponseEntity<Void> deleteByEmail(@PathVariable String email){
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteUser/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
//    @GetMapping
//    ResponseEntity<List<User>> getUsers() {
//        List<User> users = userRepository.findAll();
//        return ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/{id}")
//    ResponseEntity<User> getUser(@PathVariable long id) {
//        Optional<User> user = userRepository.findById(id);
//        return ResponseEntity.ok(user.orElse(null));
//    }
//
//    @PostMapping
//    ResponseEntity<User> createUser(@RequestBody User request) {
//        return ResponseEntity.ok(userRepository.save(request));
//    }
//
//    @PatchMapping
//    ResponseEntity<User> updateUser(@RequestBody User request) {
//        return ResponseEntity.ok(userRepository.save(request));
//    }
//
//    @DeleteMapping("/{id}")
//    ResponseEntity<User> deleteUser(@PathVariable Long id) {
//        userRepository.deleteById(id);
//        return ResponseEntity.ok(null);
//    }
