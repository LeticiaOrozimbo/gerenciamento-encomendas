package com.projeto.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.domain.encomenda.Encomenda;
import com.projeto.domain.encomenda.StatusEncomenda;
import com.projeto.domain.morador.Morador;
import com.projeto.infrastructure.persistence.EncomendaRepositoryImpl;
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

@SpringBootTest
@AutoConfigureMockMvc
class EncomendaControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private MoradorRepositoryImpl moradorRepository;
    @Autowired private EncomendaRepositoryImpl encomendaRepository;

    @BeforeEach
    void setUp() {
        encomendaRepository.deleteAll();
        moradorRepository.deleteAll();
        Morador m = new Morador();
        m.setNome("Maria Souza");
        m.setApartamento("202");
        m.setTelefone("11988887777");
        moradorRepository.salvar(m);
    }

    @Test
    void deveReceberEncomendaViaApi() throws Exception {
        Encomenda e = new Encomenda();
        e.setNomeMorador("Maria Souza");
        e.setApartamento("202");
        e.setDescricao("Notebook Dell");
        mockMvc.perform(post("/encomendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(e)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("RECEBIDA")))
            .andExpect(jsonPath("$.apartamento", is("202")));
    }

    @Test
    void deveRetirarEncomenda() throws Exception {
        Encomenda e = new Encomenda();
        e.setNomeMorador("Maria Souza");
        e.setApartamento("202");
        e.setDescricao("Livro");
        e.setStatus(StatusEncomenda.NOTIFICADA);
        Encomenda salva = encomendaRepository.salvar(e);
        mockMvc.perform(put("/encomendas/" + salva.getId() + "/retirar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("RETIRADA")));
    }

    @Test
    void deveConfirmarNotificacao() throws Exception {
        Encomenda e = new Encomenda();
        e.setNomeMorador("Maria Souza");
        e.setApartamento("202");
        e.setDescricao("Tenis");
        e.setStatus(StatusEncomenda.NOTIFICADA);
        Encomenda salva = encomendaRepository.salvar(e);
        mockMvc.perform(put("/encomendas/" + salva.getId() + "/confirmar-notificacao"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.notificacaoConfirmada", is(true)));
    }

    @Test
    void deveListarEncomendas() throws Exception {
        mockMvc.perform(get("/encomendas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void deveRetornarErroBadRequestParaApartamentoInexistente() throws Exception {
        Encomenda e = new Encomenda();
        e.setNomeMorador("Fulano");
        e.setApartamento("999");
        e.setDescricao("Pacote");
        mockMvc.perform(post("/encomendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(e)))
            .andExpect(status().isBadRequest());
    }
}
