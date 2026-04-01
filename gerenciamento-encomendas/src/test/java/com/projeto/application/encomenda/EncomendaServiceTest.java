package com.projeto.application.encomenda;

import com.projeto.domain.encomenda.Encomenda;
import com.projeto.domain.encomenda.EncomendaRepository;
import com.projeto.domain.encomenda.StatusEncomenda;
import com.projeto.domain.morador.Morador;
import com.projeto.domain.morador.MoradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EncomendaServiceTest {

    @Mock
    private EncomendaRepository encomendaRepository;
    @Mock
    private MoradorRepository moradorRepository;
    @Mock
    private EncomendaPublisher publisher;

    @InjectMocks
    private EncomendaService service;

    private Morador morador;
    private Encomenda encomenda;

    @BeforeEach
    void setUp() {
        morador = new Morador();
        morador.setNome("João Silva");
        morador.setApartamento("101");
        morador.setTelefone("11999990000");

        encomenda = new Encomenda();
        encomenda.setNomeMorador("João Silva");
        encomenda.setApartamento("101");
        encomenda.setDescricao("Caixa Amazon");
    }

    @Test
    void deveReceberEncomendaComSucesso() {
        when(moradorRepository.buscarPorApartamento("101")).thenReturn(Optional.of(morador));
        when(encomendaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Encomenda resultado = service.receberEncomenda(encomenda);

        assertThat(resultado.getStatus()).isEqualTo(StatusEncomenda.RECEBIDA);
        verify(publisher).publicar(any(Encomenda.class));
        verify(encomendaRepository).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoMoradorNaoExiste() {
        when(moradorRepository.buscarPorApartamento("999")).thenReturn(Optional.empty());
        encomenda.setApartamento("999");

        assertThatThrownBy(() -> service.receberEncomenda(encomenda))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Morador não cadastrado");
    }

    @Test
    void deveDarBaixaNaRetirada() {
        encomenda.setStatus(StatusEncomenda.NOTIFICADA);
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));
        when(encomendaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Encomenda resultado = service.retirarEncomenda(1L);

        assertThat(resultado.getStatus()).isEqualTo(StatusEncomenda.RETIRADA);
    }

    @Test
    void deveLancarExcecaoAoRetirarEncomendaInexistente() {
        when(encomendaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.retirarEncomenda(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Encomenda não encontrada");
    }

    @Test
    void deveConfirmarNotificacao() {
        encomenda.setNotificacaoConfirmada(false);
        when(encomendaRepository.buscarPorId(1L)).thenReturn(Optional.of(encomenda));
        when(encomendaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        Encomenda resultado = service.confirmarNotificacao(1L);

        assertThat(resultado.isNotificacaoConfirmada()).isTrue();
    }
}

