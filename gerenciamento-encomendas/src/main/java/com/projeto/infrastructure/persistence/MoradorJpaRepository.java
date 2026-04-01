package com.projeto.infrastructure.persistence;

import com.projeto.domain.morador.Morador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


interface MoradorJpaRepository extends JpaRepository<Morador, Long> {
    Optional<Morador> findByApartamento(String apartamento);
}

