package com.projeto.infrastructure.messaging;

import com.projeto.application.encomenda.EncomendaPublisher;
import com.projeto.domain.encomenda.Encomenda;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class EncomendaTestPublisher implements EncomendaPublisher {

    @Override
    public void publicar(Encomenda encomenda) {
        System.out.printf(
                "Encomenda publicada na fila: id=%d | apt=%s%n",
                encomenda.getId(),
                encomenda.getApartamento()
        );
    }
}
