package com.projeto.infrastructure.persistence;
import com.projeto.domain.funcionario.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface FuncionarioJpaRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByLogin(String login);
}
