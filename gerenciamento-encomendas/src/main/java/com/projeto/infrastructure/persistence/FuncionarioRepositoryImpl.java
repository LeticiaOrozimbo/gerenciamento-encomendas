package com.projeto.infrastructure.persistence;
import com.projeto.domain.funcionario.Funcionario;
import com.projeto.domain.funcionario.FuncionarioRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public class FuncionarioRepositoryImpl implements FuncionarioRepository {
    private final FuncionarioJpaRepository jpa;
    public FuncionarioRepositoryImpl(FuncionarioJpaRepository jpa) { this.jpa = jpa; }
    @Override public Funcionario salvar(Funcionario f) { return jpa.save(f); }
    @Override public Optional<Funcionario> buscarPorId(Long id) { return jpa.findById(id); }
    @Override public Optional<Funcionario> buscarPorLogin(String login) { return jpa.findByLogin(login); }
    @Override public java.util.List<Funcionario> listarTodos() { return jpa.findAll(); }
    @Override public void deletar(Long id) { jpa.deleteById(id); }
}
