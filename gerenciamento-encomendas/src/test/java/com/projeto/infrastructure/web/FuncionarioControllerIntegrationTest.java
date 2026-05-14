package com.projeto.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.domain.funcionario.Funcionario;
import com.projeto.infrastructure.persistence.FuncionarioJpaRepository;
import com.projeto.infrastructure.persistence.UsuarioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FuncionarioControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private FuncionarioJpaRepository funcionarioJpaRepository;
    @Autowired private UsuarioJpaRepository usuarioJpaRepository;

    @BeforeEach
    void setUp() {
        funcionarioJpaRepository.deleteAll();
        usuarioJpaRepository.deleteAll();
    }

    @Test
    void deveCadastrarFuncionario() throws Exception {
        Funcionario f = new Funcionario();
        f.setNome("Porteiro Carlos");
        f.setLogin("porteiro.carlos");
        f.setSenha("senha123");

        mockMvc.perform(post("/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(f)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Porteiro Carlos")))
            .andExpect(jsonPath("$.login", is("porteiro.carlos")));
    }

    @Test
    void deveListarFuncionarios() throws Exception {
        mockMvc.perform(get("/funcionarios")
                .with(user("admin").roles("PORTEIRO")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void deveBuscarFuncionarioPorId() throws Exception {
        Funcionario f = new Funcionario();
        f.setNome("Porteiro Ana");
        f.setLogin("porteiro.ana");
        f.setSenha("senha456");
        Funcionario salvo = funcionarioJpaRepository.save(f);

        mockMvc.perform(get("/funcionarios/" + salvo.getId())
                .with(user("admin").roles("PORTEIRO")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Porteiro Ana")));
    }

    @Test
    void deveAtualizarFuncionario() throws Exception {
        Funcionario f = new Funcionario();
        f.setNome("Porteiro Original");
        f.setLogin("porteiro.orig");
        f.setSenha("senha789");
        Funcionario salvo = funcionarioJpaRepository.save(f);

        Funcionario dados = new Funcionario();
        dados.setNome("Porteiro Atualizado");

        mockMvc.perform(put("/funcionarios/" + salvo.getId())
                .with(user("admin").roles("PORTEIRO"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dados)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Porteiro Atualizado")));
    }

    @Test
    void deveDeletarFuncionario() throws Exception {
        Funcionario f = new Funcionario();
        f.setNome("Porteiro Delete");
        f.setLogin("porteiro.del");
        f.setSenha("senhadel");
        Funcionario salvo = funcionarioJpaRepository.save(f);

        mockMvc.perform(delete("/funcionarios/" + salvo.getId())
                .with(user("admin").roles("PORTEIRO")))
            .andExpect(status().isNoContent());
    }
}

