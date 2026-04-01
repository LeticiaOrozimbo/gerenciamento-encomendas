package com.projeto.application.encomenda;

import com.projeto.domain.encomenda.Encomenda;
import com.projeto.domain.encomenda.EncomendaRepository;
import com.projeto.domain.encomenda.StatusEncomenda;
import com.projeto.domain.morador.MoradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Encomenda receberEncomenda(Encomenda encomenda) {
        moradorRepository.buscarPorApartamento(encomenda.getApartamento())
            .orElseThrow(() -> new RuntimeException(
                "Morador não cadastrado para o apartamento: " + encomenda.getApartamento()));

        encomenda.setStatus(StatusEncomenda.RECEBIDA);
        Encomenda salva = encomendaRepository.salvar(encomenda);

        publisher.publicar(salva);

        return salva;
    }
    public Encomenda retirarEncomenda(Long id) {
        Encomenda encomenda = encomendaRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Encomenda não encontrada: " + id));

        encomenda.setStatus(StatusEncomenda.RETIRADA);
        return encomendaRepository.salvar(encomenda);
    }
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

