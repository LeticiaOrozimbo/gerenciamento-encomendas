package com.projeto.infrastructure.messaging;

import com.projeto.domain.encomenda.Encomenda;
import com.projeto.domain.encomenda.EncomendaRepository;
import com.projeto.domain.encomenda.StatusEncomenda;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
@Profile("!dev")
public class NotificacaoConsumer {

    private final EncomendaRepository encomendaRepository;

    public NotificacaoConsumer(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    @Bean
    public Consumer<Encomenda> notificacoesMoradores() {
        return encomenda -> {
            System.out.printf(
                    "Notificacao enviada ao morador %s (Apt %s): %s%n",
                    encomenda.getNomeMorador(),
                    encomenda.getApartamento(),
                    encomenda.getDescricao()
            );

            encomendaRepository.buscarPorId(encomenda.getId()).ifPresent(e -> {
                e.setStatus(StatusEncomenda.NOTIFICADA);
                encomendaRepository.salvar(e);
            });
        };
    }
}
