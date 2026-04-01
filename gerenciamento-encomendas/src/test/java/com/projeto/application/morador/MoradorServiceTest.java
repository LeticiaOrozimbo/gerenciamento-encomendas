package com.projeto.application.morador;

import com.projeto.domain.morador.Morador;
import com.projeto.domain.morador.MoradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoradorServiceTest {

    @Mock
    private MoradorRepository moradorRepository;

    @InjectMocks
    private MoradorService service;

    private Morador morador;

    @BeforeEach
    void setUp() {
        morador = new Morador();
        morador.setNome("João Silva");
        morador.setTelefone("11999990001");
        morador.setApartamento("101");
    }

    // ── cadastrarMorador ──────────────────────────

    @Test
    void deveCadastrarMoradorComSucesso() {
        when(moradorRepository.salvar(any(Morador.class))).thenReturn(morador);

        Morador resultado = service.cadastrarMorador(morador);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getApartamento()).isEqualTo("101");
        verify(moradorRepository).salvar(morador);
    }

    @Test
    void deveDelegarSalvamentoCadastroAoRepositorio() {
        when(moradorRepository.salvar(any(Morador.class))).thenAnswer(inv -> inv.getArgument(0));

        Morador resultado = service.cadastrarMorador(morador);

        assertThat(resultado.getTelefone()).isEqualTo("11999990001");
        verify(moradorRepository, times(1)).salvar(any());
    }

    // ── atualizarMorador ──────────────────────────

    @Test
    void deveAtualizarMoradorComSucesso() {
        Morador dadosNovos = new Morador();
        dadosNovos.setNome("João Atualizado");
        dadosNovos.setTelefone("11988880001");
        dadosNovos.setApartamento("101");

        when(moradorRepository.buscarPorId(1L)).thenReturn(Optional.of(morador));
        when(moradorRepository.salvar(any(Morador.class))).thenAnswer(inv -> inv.getArgument(0));

        Morador resultado = service.atualizarMorador(1L, dadosNovos);

        assertThat(resultado.getNome()).isEqualTo("João Atualizado");
        assertThat(resultado.getTelefone()).isEqualTo("11988880001");
        assertThat(resultado.getApartamento()).isEqualTo("101");
        verify(moradorRepository).salvar(morador);
    }

    @Test
    void deveLancarExcecaoAoAtualizarMoradorInexistente() {
        when(moradorRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        Morador dadosNovos = new Morador();
        dadosNovos.setNome("Qualquer");
        dadosNovos.setTelefone("11900000000");
        dadosNovos.setApartamento("999");

        assertThatThrownBy(() -> service.atualizarMorador(99L, dadosNovos))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Morador não encontrado: 99");

        verify(moradorRepository, never()).salvar(any());
    }

    // ── listarMoradores ───────────────────────────

    @Test
    void deveListarTodosOsMoradores() {
        Morador morador2 = new Morador();
        morador2.setNome("Maria Souza");
        morador2.setTelefone("11999990002");
        morador2.setApartamento("202");

        when(moradorRepository.listarTodos()).thenReturn(List.of(morador, morador2));

        List<Morador> resultado = service.listarMoradores();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(Morador::getApartamento)
                .containsExactlyInAnyOrder("101", "202");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaMoradores() {
        when(moradorRepository.listarTodos()).thenReturn(List.of());

        List<Morador> resultado = service.listarMoradores();

        assertThat(resultado).isEmpty();
        verify(moradorRepository).listarTodos();
    }

    // ── buscarPorApartamento ──────────────────────

    @Test
    void deveBuscarMoradorPorApartamentoComSucesso() {
        when(moradorRepository.buscarPorApartamento("101")).thenReturn(Optional.of(morador));

        Morador resultado = service.buscarPorApartamento("101");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getApartamento()).isEqualTo("101");
    }

    @Test
    void deveLancarExcecaoQuandoApartamentoNaoExiste() {
        when(moradorRepository.buscarPorApartamento("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorApartamento("999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Morador não encontrado no apartamento: 999");
    }
}

