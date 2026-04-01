package com.projeto.infrastructure.persistence;

import com.projeto.domain.morador.Morador;
import com.projeto.domain.morador.MoradorRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MoradorRepositoryImpl implements MoradorRepository {

    private final MoradorJpaRepository jpa;

    public MoradorRepositoryImpl(MoradorJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Morador salvar(Morador morador) {
        return jpa.save(morador);
    }

    @Override
    public Optional<Morador> buscarPorId(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Morador> buscarPorApartamento(String apartamento) {
        return jpa.findByApartamento(apartamento);
    }

    @Override
    public List<Morador> listarTodos() {
        return jpa.findAll();
    }

    public void deleteAll() {
        jpa.deleteAll();
    }
}


