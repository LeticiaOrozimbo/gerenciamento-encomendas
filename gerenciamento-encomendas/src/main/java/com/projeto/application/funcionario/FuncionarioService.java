package com.projeto.application.funcionario;

import com.projeto.domain.funcionario.Funcionario;
import com.projeto.domain.funcionario.FuncionarioRepository;
import com.projeto.domain.usuario.Perfil;
import com.projeto.domain.usuario.Usuario;
import com.projeto.infrastructure.persistence.UsuarioJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso responsável pelo CRUD de funcionários (porteiros) do condomínio.
 *
 * <p>Ao cadastrar um funcionário, o serviço:
 * <ol>
 *   <li>Codifica a senha com BCrypt antes de persistir</li>
 *   <li>Cria um {@link com.projeto.domain.usuario.Usuario} com perfil
 *       {@link com.projeto.domain.usuario.Perfil#PORTEIRO}</li>
 * </ol>
 *
 * @author Equipe Projeto
 */
@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository,
                              UsuarioJpaRepository usuarioJpaRepository,
                              PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Funcionario cadastrar(Funcionario funcionario) {
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        Funcionario salvo = funcionarioRepository.salvar(funcionario);

        Usuario usuario = new Usuario();
        usuario.setUsername(funcionario.getLogin());
        usuario.setPassword(salvo.getSenha());
        usuario.setPerfil(Perfil.PORTEIRO);
        usuarioJpaRepository.save(usuario);

        return salvo;
    }

    public List<Funcionario> listar() {
        return funcionarioRepository.listarTodos();
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado: " + id));
    }

    public Funcionario atualizar(Long id, Funcionario dados) {
        Funcionario funcionario = buscarPorId(id);
        if (dados.getNome() != null) funcionario.setNome(dados.getNome());
        if (dados.getLogin() != null) funcionario.setLogin(dados.getLogin());
        if (dados.getSenha() != null) funcionario.setSenha(passwordEncoder.encode(dados.getSenha()));
        return funcionarioRepository.salvar(funcionario);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        funcionarioRepository.deletar(id);
    }
}

