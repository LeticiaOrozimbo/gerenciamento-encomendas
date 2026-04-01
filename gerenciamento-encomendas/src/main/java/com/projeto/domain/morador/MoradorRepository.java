package com.projeto.domain.morador;

import java.util.List;
import java.util.Optional;

public interface MoradorRepository {
    Morador salvar(Morador morador);

    Optional<Morador> buscarPorId(Long id);

    Optional<Morador> buscarPorApartamento(String apartamento);

    List<Morador> listarTodos();
}

