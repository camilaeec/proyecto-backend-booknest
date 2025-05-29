package com.example.booknest.user.application;

import com.example.booknest.book.domain.Book;
import com.example.booknest.user.domain.UserService;
import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import com.example.booknest.user.dto.UserRequestDTO;
import com.example.booknest.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<UserResponseForOtherUsersDTO> getByEmail(@PathVariable String email){
        UserResponseForOtherUsersDTO userResponseDTO = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @GetMapping("/getById/id/{id}")
    public ResponseEntity<UserResponseForOtherUsersDTO> getById(@PathVariable Long id){
        UserResponseForOtherUsersDTO userResponseDTO = userService.getUserById(id);
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

    @GetMapping("/me/books")
    @PreAuthorize("hasAnyRole('COMMON_USER', 'ADMIN')")
    public ResponseEntity<List<Book>> getMyBooks() {
        return ResponseEntity.ok(userService.getBooksByCurrentUser());
    }
}
