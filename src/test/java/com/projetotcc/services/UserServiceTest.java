package com.projetotcc.services;

import com.projetotcc.domain.user.User;
import com.projetotcc.domain.user.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class UserServiceTest {

    // validateTransaction não usa o repositório, então não precisa de mock.
    private final UserService userService = new UserService();

    @Test
    @DisplayName("Lançar Exception quando um lojista tenta enviar uma transferência")
    void validateTransactionMerchant() {
        User lojista = new User(1L, "Loja", "Silva", "03030303034", "loja@hotmail.com", "12345", new BigDecimal(100), UserType.MERCHANT);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            userService.validateTransaction(lojista, new BigDecimal(50));
        });

        Assertions.assertEquals("Usuário não autorizado a realizar transferência", thrown.getMessage());
    }

    @Test
    @DisplayName("Lançar Exception quando o saldo é menor que o valor da transferência")
    void validateTransactionInsufficientBalance() {
        User comum = new User(2L, "Joana", "Silva", "01010101012", "joana@hotmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            userService.validateTransaction(comum, new BigDecimal(50));
        });

        Assertions.assertEquals("Saldo insuficiente para realizar a transfêrencia", thrown.getMessage());
    }
}
