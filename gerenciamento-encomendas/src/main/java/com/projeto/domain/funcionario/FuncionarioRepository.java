package com.projeto.domain.funcionario;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository {
    Funcionario salvar(Funcionario funcionario);
    Optional<Funcionario> buscarPorId(Long id);
    Optional<Funcionario> buscarPorLogin(String login);
    List<Funcionario> listarTodos();
    void deletar(Long id);
}

