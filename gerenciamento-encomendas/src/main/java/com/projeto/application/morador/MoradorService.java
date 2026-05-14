package com.projeto.application.morador;

import com.projeto.domain.morador.Morador;
import com.projeto.domain.morador.MoradorRepository;
import com.projeto.domain.usuario.Perfil;
import com.projeto.domain.usuario.Usuario;
import com.projeto.infrastructure.persistence.UsuarioJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Caso de uso responsável pelo cadastro e manutenção dos moradores do condomínio.
 *
 * <p>Ao cadastrar um morador com {@code login} e {@code senha} informados,
 * cria automaticamente um {@link com.projeto.domain.usuario.Usuario} com perfil
 * {@link com.projeto.domain.usuario.Perfil#MORADOR} para autenticação via JWT.</p>
 *
 * @author Equipe Projeto
 */
@Service
public class MoradorService {

    private final MoradorRepository moradorRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public MoradorService(MoradorRepository moradorRepository,
                          UsuarioJpaRepository usuarioJpaRepository,
                          PasswordEncoder passwordEncoder) {
        this.moradorRepository = moradorRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Morador cadastrarMorador(Morador morador) {
        Morador salvo = moradorRepository.salvar(morador);

        if (morador.getLogin() != null && morador.getSenha() != null) {
            Usuario usuario = new Usuario();
            usuario.setUsername(morador.getLogin());
            usuario.setPassword(passwordEncoder.encode(morador.getSenha()));
            usuario.setPerfil(Perfil.MORADOR);
            usuarioJpaRepository.save(usuario);
        }

        return salvo;
    }

    public Morador atualizarMorador(Long id, Morador dados) {
        Morador morador = moradorRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Morador não encontrado: " + id));

        morador.setNome(dados.getNome());
        morador.setTelefone(dados.getTelefone());
        morador.setApartamento(dados.getApartamento());
        return moradorRepository.salvar(morador);
    }

    public List<Morador> listarMoradores() {
        return moradorRepository.listarTodos();
    }

    public Morador buscarPorApartamento(String apartamento) {
        return moradorRepository.buscarPorApartamento(apartamento)
                .orElseThrow(() -> new RuntimeException("Morador não encontrado no apartamento: " + apartamento));
    }
}
