package com.example.booknest.transaction.domain;

import com.example.booknest.book.domain.Book;
import com.example.booknest.book.dto.BookResponse;
import com.example.booknest.book.infraestructure.BookRepository;
import com.example.booknest.exception.ResourceNotFoundException;
import com.example.booknest.transaction.dto.TransactionRequestDTO;
import com.example.booknest.transaction.dto.TransactionResponseDTO;
import com.example.booknest.transaction.infraestructure.TransactionRepository;
import com.example.booknest.user.domain.User;
import com.example.booknest.user.domain.UserService;
import com.example.booknest.user.dto.UserResponseForOtherUsersDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              UserService userService,
                              BookRepository bookRepository,
                              ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    private UserResponseForOtherUsersDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(username);
    }

    @Transactional
    public Transaction createExchangeTransaction(TransactionRequestDTO transactionRequestDTO) {
        UserResponseForOtherUsersDTO buyerDTO = getCurrentUser();
        User buyer = userService.getMeLocal();

        Book bookWanted = bookRepository.findById(transactionRequestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro deseado no encontrado"));

        Book bookOffered = bookRepository.findById(transactionRequestDTO.getOfferedBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro ofrecido no encontrado"));

        if (!bookOffered.getUser().getNickname().equals(buyerDTO.getNickname())) {
            throw new RuntimeException("El libro ofrecido debe pertenecer al comprador");
        }

        if (bookWanted.getUser().getId().equals(buyer.getId())) {
            throw new RuntimeException("No puedes intercambiar un libro contigo mismo.");
        }

        boolean yaAceptado = transactionRepository
                .findByBook_IdBook(transactionRequestDTO.getBookId())
                .stream()
                .anyMatch(t -> Boolean.TRUE.equals(t.getAccepted()));

        if (yaAceptado) {
            throw new RuntimeException("El libro ya fue intercambiado");
        }
        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(bookWanted.getUser());
        transaction.setBook(bookWanted);
        transaction.setDate(new Date());
        transaction.setAccepted(null);
        transaction.setOfferedBook(bookOffered);

        return transactionRepository.save(transaction);
    }

    public TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setIdTransaction(transaction.getIdTransaction());
        responseDTO.setDate(transaction.getDate());
        responseDTO.setAccepted(transaction.getAccepted());

        // Mapear comprador
        if (transaction.getBuyer() != null) {
            UserResponseForOtherUsersDTO buyerDTO = new UserResponseForOtherUsersDTO();
            buyerDTO.setId(transaction.getBuyer().getId());
            buyerDTO.setNickname(transaction.getBuyer().getNickname());
            responseDTO.setBuyer(buyerDTO);
        }

        // Mapear vendedor
        if (transaction.getSeller() != null) {
            UserResponseForOtherUsersDTO sellerDTO = new UserResponseForOtherUsersDTO();
            sellerDTO.setId(transaction.getSeller().getId());
            sellerDTO.setNickname(transaction.getSeller().getNickname());
            responseDTO.setSeller(sellerDTO);
        }

        // Mapear libro principal
        if (transaction.getBook() != null) {
            BookResponse bookResponse = modelMapper.map(transaction.getBook(), BookResponse.class);
            // Mapear owner del libro si es necesario
            if (transaction.getBook().getUser() != null) {
                UserResponseForOtherUsersDTO ownerDTO = new UserResponseForOtherUsersDTO();
                ownerDTO.setId(transaction.getBook().getUser().getId());
                ownerDTO.setNickname(transaction.getBook().getUser().getNickname());
                bookResponse.setOwner(ownerDTO);
            }
            responseDTO.setBook(bookResponse);
        }

        // Mapear libro ofrecido
        if (transaction.getOfferedBook() != null) {
            BookResponse offeredBookResponse = modelMapper.map(transaction.getOfferedBook(), BookResponse.class);
            // Mapear owner del libro ofrecido si es necesario
            if (transaction.getOfferedBook().getUser() != null) {
                UserResponseForOtherUsersDTO ownerDTO = new UserResponseForOtherUsersDTO();
                ownerDTO.setId(transaction.getOfferedBook().getUser().getId());
                ownerDTO.setNickname(transaction.getOfferedBook().getUser().getNickname());
                offeredBookResponse.setOwner(ownerDTO);
            }
            responseDTO.setOfferedBook(offeredBookResponse);
        }

        return responseDTO;
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(long id) {
        Transaction transaction = transactionRepository.findById(id);
        return convertToResponseDTO(transaction);
    }

    @Transactional
    public TransactionResponseDTO updateTransactionAcceptance(Long transactionId, Boolean accepted) {
        UserResponseForOtherUsersDTO sellerDTO = getCurrentUser();
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        if (transaction.getSeller().getNickname().equals(sellerDTO.getNickname())) {
            throw new RuntimeException("Solo el vendedor puede aceptar/rechazar la transacción");
        }

        transaction.setAccepted(accepted);

        if (accepted) {
            transactionRepository.deleteOtherTransactionsForBook(
                    transaction.getBook().getIdBook(),
                    transactionId
            );
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToResponseDTO(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        UserResponseForOtherUsersDTO sellerDTO = getCurrentUser();
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        if (transaction.getSeller().getNickname().equals(sellerDTO.getNickname())) {
            throw new RuntimeException("Solo el vendedor puede eliminar la transacción");
        }

        if (Boolean.TRUE.equals(transaction.getAccepted())) {
            throw new RuntimeException("No se puede eliminar una transacción ya aceptada");
        }

        transactionRepository.delete(transaction);
    }

    public List<TransactionResponseDTO> getTransactionsByBook(Long bookId) {
        return transactionRepository.findByBook_IdBook(bookId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByBuyer(Long buyerId) {
        return transactionRepository.findByBuyer_Id(buyerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsBySeller(Long sellerId) {
        return transactionRepository.findBySeller_Id(sellerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public boolean isTransactionParticipant(String username, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        UserResponseForOtherUsersDTO user = userService.getUserByEmail(username);

        return (transaction.getBuyer() != null && transaction.getBuyer().getId().equals(user.getId())) ||
                (transaction.getSeller() != null && transaction.getSeller().getId().equals(user.getId()));
    }

    public boolean isTransactionSeller(String username, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        if (transaction == null) return false;

        UserResponseForOtherUsersDTO user = userService.getUserByEmail(username);

        return transaction.getSeller() != null && transaction.getSeller().getId().equals(user.getId());
    }

    public boolean isBookOwner(String username, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        UserResponseForOtherUsersDTO user = userService.getUserByEmail(username);
        return book.getUser() != null && book.getUser().getId().equals(user.getId());
    }
}