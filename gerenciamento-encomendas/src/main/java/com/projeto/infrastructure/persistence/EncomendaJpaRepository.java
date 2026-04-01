package com.projeto.infrastructure.persistence;

import com.projeto.domain.encomenda.Encomenda;
import org.springframework.data.jpa.repository.JpaRepository;

interface EncomendaJpaRepository extends JpaRepository<Encomenda, Long> {}

