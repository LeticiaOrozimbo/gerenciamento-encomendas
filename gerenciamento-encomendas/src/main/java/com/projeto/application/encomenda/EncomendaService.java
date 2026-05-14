package com.projeto.application.encomenda;

import com.projeto.domain.encomenda.Encomenda;
import com.projeto.domain.encomenda.EncomendaRepository;
import com.projeto.domain.encomenda.StatusEncomenda;
import com.projeto.domain.morador.MoradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso responsável pelo ciclo de vida das encomendas.
 *
 * <p>Implementa as seguintes regras de negócio:
 * <ul>
 *   <li>Validação de que o apartamento informado possui morador cadastrado</li>
 *   <li>Publicação de evento de mensageria ao receber uma encomenda</li>
 *   <li>Atualização de status na retirada e na confirmação de notificação</li>
 * </ul>
 *
 * <p>Esta classe pertence à camada de <strong>Application</strong> (casos de uso)
 * da Clean Architecture e não depende de nenhum framework externo.</p>
 *
 * @author Equipe Projeto
 */
@Service
public class EncomendaService {

    private final EncomendaRepository encomendaRepository;
    private final MoradorRepository moradorRepository;
    private final EncomendaPublisher publisher;

    public EncomendaService(EncomendaRepository encomendaRepository,
                            MoradorRepository moradorRepository,
                            EncomendaPublisher publisher) {
        this.encomendaRepository = encomendaRepository;
        this.moradorRepository = moradorRepository;
        this.publisher = publisher;
    }

    /**
     * Registra a chegada de uma encomenda na portaria.
     *
     * <p>Valida se existe morador cadastrado para o apartamento informado,
     * define o status como {@link StatusEncomenda#RECEBIDA} e publica o evento
     * na fila de mensageria para notificação ao morador.</p>
     *
     * @param encomenda dados da encomenda recebida
     * @return encomenda persistida com id e status preenchidos
     * @throws RuntimeException se não existir morador no apartamento
     */
    public Encomenda receberEncomenda(Encomenda encomenda) {
        moradorRepository.buscarPorApartamento(encomenda.getApartamento())
            .orElseThrow(() -> new RuntimeException(
                "Morador não cadastrado para o apartamento: " + encomenda.getApartamento()));

        encomenda.setStatus(StatusEncomenda.RECEBIDA);
        Encomenda salva = encomendaRepository.salvar(encomenda);

        publisher.publicar(salva);

        return salva;
    }
    /**
     * Registra a retirada de uma encomenda pelo morador na portaria.
     *
     * @param id identificador da encomenda
     * @return encomenda com status {@link StatusEncomenda#RETIRADA}
     * @throws RuntimeException se a encomenda não for encontrada
     */
    public Encomenda retirarEncomenda(Long id) {
        Encomenda encomenda = encomendaRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Encomenda não encontrada: " + id));

        encomenda.setStatus(StatusEncomenda.RETIRADA);
        return encomendaRepository.salvar(encomenda);
    }
    /**
     * Confirma que o morador recebeu a notificação de chegada da encomenda.
     *
     * @param id identificador da encomenda
     * @return encomenda com {@code notificacaoConfirmada = true}
     * @throws RuntimeException se a encomenda não for encontrada
     */
    public Encomenda confirmarNotificacao(Long id) {
        Encomenda encomenda = encomendaRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Encomenda não encontrada: " + id));

        encomenda.setNotificacaoConfirmada(true);
        return encomendaRepository.salvar(encomenda);
    }

    public List<Encomenda> listarEncomendas() {
        return encomendaRepository.listarTodos();
    }

    public Encomenda buscarEncomenda(Long id) {
        return encomendaRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Encomenda não encontrada: " + id));
    }
}

