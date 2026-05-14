package com.projeto.domain.encomenda;

import java.util.List;
import java.util.Optional;

/**
 * Porta de saída (output port) da camada de domínio para persistência de {@link Encomenda}.
 *
 * <p>Seguindo os princípios da Clean Architecture, esta interface define o contrato
 * de acesso a dados sem depender de nenhum framework de persistência. A implementação
 * concreta reside na camada de infraestrutura ({@code EncomendaRepositoryImpl}).</p>
 *
 * @author Equipe Projeto
 */
public interface EncomendaRepository {

    /**
     * Persiste ou atualiza uma encomenda.
     *
     * @param encomenda entidade a salvar; nunca {@code null}
     * @return entidade salva com id preenchido
     */
    Encomenda salvar(Encomenda encomenda);

    /**
     * Busca uma encomenda pelo seu identificador único.
     *
     * @param id identificador da encomenda
     * @return {@link Optional} com a encomenda, ou vazio se não encontrada
     */
    Optional<Encomenda> buscarPorId(Long id);

    /**
     * Retorna todas as encomendas cadastradas.
     *
     * @return lista imutável de encomendas (pode ser vazia)
     */
    List<Encomenda> listarTodos();
}

