package com.example.booknest.transaction.application;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.infraestructure.BookRepository;
import com.example.booknest.transaction.domain.Transaction;
import com.example.booknest.transaction.domain.TransactionService;
import com.example.booknest.transaction.dto.TransactionRequestDTO;
import com.example.booknest.transaction.dto.TransactionResponseDTO;
import com.example.booknest.user.domain.User;
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

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User buyer;
    private User seller;
    private Book bookWanted;
    private Book bookOffered;

    @BeforeEach
    public void setUp() {
        // Crear y guardar usuarios
        buyer = new User();
        buyer.setNickname("buyer1");
        buyer.setEmail("buyer@example.com");
        buyer.setPassword("password");
        buyer.setName("Buyer");
        buyer.setLastname("Test");
        buyer.setPhoneNumber("123456789");
        buyer = userRepository.save(buyer);

        seller = new User();
        seller.setNickname("seller1");
        seller.setEmail("seller@example.com");
        seller.setPassword("password");
        seller.setName("Seller");
        seller.setLastname("Test");
        seller.setPhoneNumber("987654321");
        seller = userRepository.save(seller);

        // Crear y guardar libros
        bookWanted = new Book();
        bookWanted.setTitle("Libro deseado");
        bookWanted.setUser(seller);
        bookWanted = bookRepository.save(bookWanted);

        bookOffered = new Book();
        bookOffered.setTitle("Libro ofrecido");
        bookOffered.setUser(buyer);
        bookOffered = bookRepository.save(bookOffered);
    }

    @Test
    @WithMockUser(username = "buyer@example.com")
    void createExchangeTransaction_ShouldReturnCreated() throws Exception {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setBookId(bookWanted.getIdBook());
        requestDTO.setOfferedBookId(bookOffered.getIdBook());
        requestDTO.setSellerNickname(seller.getNickname());

        mockMvc.perform(post("/transactions/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTransaction").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTransactions_ShouldReturnOk() throws Exception {
        // Crear una transacción de prueba
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setBookId(bookWanted.getIdBook());
        requestDTO.setOfferedBookId(bookOffered.getIdBook());
        requestDTO.setSellerNickname(seller.getNickname());
        transactionService.createExchangeTransaction(requestDTO);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTransaction").exists());
    }

    @Test
    @WithMockUser(username = "buyer1")
    void getTransactionById_ShouldReturnOk() throws Exception {
        // Crear una transacción de prueba
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setBookId(bookWanted.getIdBook());
        requestDTO.setOfferedBookId(bookOffered.getIdBook());
        requestDTO.setSellerNickname(seller.getNickname());
        Transaction transaction = transactionService.createExchangeTransaction(requestDTO);

        mockMvc.perform(get("/transactions/{id}", transaction.getIdTransaction()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTransaction").value(transaction.getIdTransaction()));
    }

    @Test
    @WithMockUser(username = "seller1")
    void updateTransactionAcceptance_ShouldReturnOk() throws Exception {
        // Crear una transacción de prueba
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setBookId(bookWanted.getIdBook());
        requestDTO.setOfferedBookId(bookOffered.getIdBook());
        requestDTO.setSellerNickname(seller.getNickname());
        Transaction transaction = transactionService.createExchangeTransaction(requestDTO);

        mockMvc.perform(patch("/transactions/{id}/acceptance", transaction.getIdTransaction())
                        .param("accepted", "true"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "seller1")
    void deleteTransaction_ShouldReturnNoContent() throws Exception {
        // Crear una transacción de prueba
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setBookId(bookWanted.getIdBook());
        requestDTO.setOfferedBookId(bookOffered.getIdBook());
        requestDTO.setSellerNickname(seller.getNickname());
        Transaction transaction = transactionService.createExchangeTransaction(requestDTO);

        mockMvc.perform(delete("/transactions/{id}", transaction.getIdTransaction()))
                .andExpect(status().isNoContent());
    }
}