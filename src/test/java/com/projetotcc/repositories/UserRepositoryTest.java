package com.projetotcc.repositories;

import com.projetotcc.domain.user.User;
import com.projetotcc.domain.user.UserType;
import com.projetotcc.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Cenário de sucesso")
    void findUserByDocumentCase1() {
        String documento = "01010101";
        UserDTO data = new UserDTO("Gabriela", "Teste", documento,new BigDecimal(10),"gabriela@email.com", "teste", UserType.COMMON);
        this.createUser(data);

        Optional <User> result =  this.userRepository.findUserByDocument(documento);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Não deve pegar o usuário do DB quando o susuários específico nao existe")
    void findUserByDocumentCase2() {
        String documento = "01010101";

        Optional <User> result =  this.userRepository.findUserByDocument(documento);

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDTO data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}