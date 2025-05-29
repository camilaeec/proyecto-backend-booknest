package com.example.booknest.book.application;

import com.example.booknest.book.domain.BookService;
import com.example.booknest.book.dto.BookResponse;
import com.example.booknest.book.dto.CreateBookRequest;
import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private CreateBookRequest createBookRequest;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        // Configurar request
        createBookRequest = new CreateBookRequest();
        createBookRequest.setTitle("Effective Java");
        createBookRequest.setAuthors(Collections.singletonList("Joshua Bloch"));
        createBookRequest.setTags(Collections.singletonList("Programming"));
        createBookRequest.setPublisher("Addison-Wesley");
        createBookRequest.setYearOfPublication("2008");
        createBookRequest.setState("Nuevo");
        createBookRequest.setBookPhotos(Collections.singletonList("photo1.jpg"));

        // Configurar response
        bookResponse = new BookResponse();
        bookResponse.setIdBook(1L);
        bookResponse.setTitle("Effective Java");
        bookResponse.setAuthors(Collections.singletonList("Joshua Bloch"));
        bookResponse.setTags(Collections.singletonList("Programming"));
        bookResponse.setPublisher("Addison-Wesley");
        bookResponse.setYearOfPublication("2008");
        bookResponse.setState("Nuevo");
        bookResponse.setBookPhotos(Collections.singletonList("photo1.jpg"));
        bookResponse.setCreatedAt(LocalDateTime.now());
        bookResponse.setUpdatedAt(LocalDateTime.now());

        UserResponseForOtherUsersDTO owner = new UserResponseForOtherUsersDTO();
        owner.setNickname("booklover");
        bookResponse.setOwner(owner);
    }

    @Test
    void createBook_ValidRequest_ReturnsCreated() throws Exception {
        when(bookService.createBook(any(CreateBookRequest.class))).thenReturn(bookResponse);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idBook").value(1L))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.authors[0]").value("Joshua Bloch"))
                .andExpect(jsonPath("$.owner.nickname").value("booklover"));
    }

    @Test
    void getBookById_ValidId_ReturnsOk() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookResponse));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBook").value(1L))
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void getBookById_InvalidId_ReturnsNotFound() throws Exception {
        when(bookService.getBookById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBooksByTitle_ReturnsOk() throws Exception {
        when(bookService.getByTitle("Effective Java")).thenReturn(Collections.singletonList(bookResponse));

        mockMvc.perform(get("/books/title/Effective Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idBook").value(1L))
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }

    @Test
    void getBooksByAuthor_ReturnsOk() throws Exception {
        when(bookService.getByAuthor("Joshua Bloch")).thenReturn(Collections.singletonList(bookResponse));

        mockMvc.perform(get("/books/author/Joshua Bloch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idBook").value(1L))
                .andExpect(jsonPath("$[0].authors[0]").value("Joshua Bloch"));
    }

    @Test
    void getBooksByTag_ReturnsOk() throws Exception {
        when(bookService.getByTag("Programming")).thenReturn(Collections.singletonList(bookResponse));

        mockMvc.perform(get("/books/tag/Programming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idBook").value(1L))
                .andExpect(jsonPath("$[0].tags[0]").value("Programming"));
    }

    @Test
    void deleteBook_ReturnsNoContent() throws Exception {
        // Simular la eliminación (void method)
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        // Verificar que se llamó al servicio
        verify(bookService, times(1)).deleteBook(1L);
    }
}