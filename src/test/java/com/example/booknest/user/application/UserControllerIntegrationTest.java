package com.example.booknest.user.application;

import com.example.booknest.user.domain.User;
import com.example.booknest.user.dto.UserRequestDTO;
import com.example.booknest.user.dto.UserResponseDTO;
import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import com.example.booknest.user.infraestructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setNickname("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setName("Test");
        testUser.setLastname("User");
        testUser.setPhoneNumber("123456789");
        testUser = userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getMe_ShouldReturnUserInfo() throws Exception {
        mockMvc.perform(get("/users/getMe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nickname").value("testuser"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateMe_ShouldUpdateUserInfo() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("Updated");
        requestDTO.setLastname("Name");

        mockMvc.perform(patch("/users/udpateMe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.lastname").value("Name"));
    }

    @Test
    void getByEmail_ShouldReturnUserInfo() throws Exception {
        mockMvc.perform(get("/users/getByEmail/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nickname").value("testuser"));
    }

    @Test
    void getById_ShouldReturnUserInfo() throws Exception {
        mockMvc.perform(get("/users/getById/id/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.nickname").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteByEmail_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/deleteUser/delete/test@example.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getMyBooks_ShouldReturnBooks() throws Exception {
        mockMvc.perform(get("/users/me/books"))
                .andExpect(status().isOk());
    }
}
