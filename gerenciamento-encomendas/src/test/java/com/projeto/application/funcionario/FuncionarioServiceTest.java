package com.projeto.application.funcionario;
import com.projeto.domain.funcionario.Funcionario;
import com.projeto.domain.funcionario.FuncionarioRepository;
import com.projeto.domain.usuario.Perfil;
import com.projeto.domain.usuario.Usuario;
import com.projeto.infrastructure.persistence.UsuarioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {
    @Mock private FuncionarioRepository funcionarioRepository;
    @Mock private UsuarioJpaRepository usuarioJpaRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks
    private FuncionarioService service;
    private Funcionario funcionario;
    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setNome("Porteiro Joao");
        funcionario.setLogin("porteiro.joao");
        funcionario.setSenha("senha123");
    }
    @Test
    void deveCadastrarFuncionarioComSucesso() {
        when(passwordEncoder.encode(any())).thenReturn("$hashed$");
        when(funcionarioRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(usuarioJpaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Funcionario resultado = service.cadastrar(funcionario);
        assertThat(resultado.getNome()).isEqualTo("Porteiro Joao");
        assertThat(resultado.getSenha()).isEqualTo("$hashed$");
        verify(funcionarioRepository).salvar(funcionario);
        verify(usuarioJpaRepository).save(any(Usuario.class));
    }
    @Test
    void deveCriarUsuarioComPerfilPorteiro() {
        when(passwordEncoder.encode(any())).thenReturn("$hashed$");
        when(funcionarioRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        service.cadastrar(funcionario);
        verify(usuarioJpaRepository).save(argThat(u -> u.getPerfil() == Perfil.PORTEIRO
                && u.getUsername().equals("porteiro.joao")));
    }
    @Test
    void deveListarFuncionarios() {
        when(funcionarioRepository.listarTodos()).thenReturn(List.of(funcionario));
        List<Funcionario> lista = service.listar();
        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Porteiro Joao");
    }
    @Test
    void deveBuscarFuncionarioPorId() {
        when(funcionarioRepository.buscarPorId(1L)).thenReturn(Optional.of(funcionario));
        Funcionario resultado = service.buscarPorId(1L);
        assertThat(resultado.getNome()).isEqualTo("Porteiro Joao");
    }
    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(funcionarioRepository.buscarPorId(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }
    @Test
    void deveAtualizarNomeFuncionario() {
        when(funcionarioRepository.buscarPorId(1L)).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        Funcionario dados = new Funcionario();
        dados.setNome("Porteiro Atualizado");
        Funcionario resultado = service.atualizar(1L, dados);
        assertThat(resultado.getNome()).isEqualTo("Porteiro Atualizado");
    }
    @Test
    void deveDeletarFuncionario() {
        when(funcionarioRepository.buscarPorId(1L)).thenReturn(Optional.of(funcionario));
        service.deletar(1L);
        verify(funcionarioRepository).deletar(1L);
    }
    @Test
    void deveLancarExcecaoAoDeletarIdInexistente() {
        when(funcionarioRepository.buscarPorId(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deletar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }
}
