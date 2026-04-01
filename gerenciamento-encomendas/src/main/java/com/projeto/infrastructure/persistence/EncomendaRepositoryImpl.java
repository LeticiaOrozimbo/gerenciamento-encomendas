package com.projeto.infrastructure.persistence;

import com.projeto.domain.encomenda.Encomenda;
import com.projeto.domain.encomenda.EncomendaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EncomendaRepositoryImpl implements EncomendaRepository {

    private final EncomendaJpaRepository jpa;

    public EncomendaRepositoryImpl(EncomendaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Encomenda salvar(Encomenda encomenda) {
        return jpa.save(encomenda);
    }

    @Override
    public Optional<Encomenda> buscarPorId(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Encomenda> listarTodos() {
        return jpa.findAll();
    }

    public void deleteAll() {
        jpa.deleteAll();
    }
}


