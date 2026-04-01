package com.projeto.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.domain.morador.Morador;
import com.projeto.infrastructure.persistence.MoradorRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração dos endpoints de morador.
 * Sobe o contexto completo com H2 e test-binder (sem RabbitMQ real).
 */
@SpringBootTest
@AutoConfigureMockMvc
class MoradorControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private MoradorRepositoryImpl moradorRepository;

    @BeforeEach
    void setUp() {
        moradorRepository.deleteAll();
    }

    @Test
    void deveCadastrarMorador() throws Exception {
        Morador m = new Morador();
        m.setNome("Carlos Andrade");
        m.setTelefone("11977776666");
        m.setApartamento("303");

        mockMvc.perform(post("/moradores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(m)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.apartamento", is("303")))
            .andExpect(jsonPath("$.telefone", is("11977776666")));
    }

    @Test
    void deveAtualizarDadosMorador() throws Exception {
        Morador m = new Morador();
        m.setNome("Carlos Andrade");
        m.setTelefone("11977776666");
        m.setApartamento("303");
        Morador salvo = moradorRepository.salvar(m);

        salvo.setTelefone("11955554444");
        salvo.setApartamento("304");

        mockMvc.perform(put("/moradores/" + salvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(salvo)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.telefone", is("11955554444")))
            .andExpect(jsonPath("$.apartamento", is("304")));
    }

    @Test
    void deveBuscarMoradorPorApartamento() throws Exception {
        Morador m = new Morador();
        m.setNome("Ana Lima");
        m.setTelefone("11944443333");
        m.setApartamento("501");
        moradorRepository.salvar(m);

        mockMvc.perform(get("/moradores/apartamento/501"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Ana Lima")));
    }

    @Test
    void deveListarMoradores() throws Exception {
        mockMvc.perform(get("/moradores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void deveRetornarErroBadRequestParaMoradorInexistente() throws Exception {
        mockMvc.perform(get("/moradores/apartamento/999"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.erro", containsString("Morador não encontrado")));
    }
}

