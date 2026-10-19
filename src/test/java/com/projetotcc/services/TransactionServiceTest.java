package com.projetotcc.services;

import com.projetotcc.domain.user.User;
import com.projetotcc.domain.user.UserType;
import com.projetotcc.dtos.TransactionDTO;
import com.projetotcc.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthorizationService authService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    @DisplayName("Criar transação com sucesso caso todas as regras sejam atendidas")
    void createTransactionCase1() throws Exception{
        User sender = new User(1L,"Joana", "Silva", "01010101012","maria@hotmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L,"Francisco", "Silva", "020202023","francisco@hotmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10),1L, 2L);
        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);
    }

    @Test
    @DisplayName("Lançar Exception quando a transação não está autorizada")
    void createTransactionCase2() throws Exception{
        User sender = new User(1L,"Joana", "Silva", "01010101012","maria@hotmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L,"Francisco", "Silva", "020202023","francisco@hotmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction(any(), any())).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () ->{
            TransactionDTO request = new TransactionDTO(new BigDecimal(10),1L, 2L);
            transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada", thrown.getMessage());
    }
}