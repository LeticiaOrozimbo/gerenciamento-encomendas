package com.projeto.domain.encomenda;

import java.util.List;
import java.util.Optional;

public interface EncomendaRepository {
    Encomenda salvar(Encomenda encomenda);

    Optional<Encomenda> buscarPorId(Long id);

    List<Encomenda> listarTodos();
}

