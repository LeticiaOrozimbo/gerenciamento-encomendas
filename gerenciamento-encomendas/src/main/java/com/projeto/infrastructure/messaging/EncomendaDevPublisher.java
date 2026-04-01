package com.projeto.infrastructure.messaging;

import com.projeto.application.encomenda.EncomendaPublisher;
import com.projeto.domain.encomenda.Encomenda;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class EncomendaDevPublisher implements EncomendaPublisher {

    @Override
    public void publicar(Encomenda encomenda) {
        System.out.printf(
                "Encomenda recebida na fila: id=%d | morador=%s | apt=%s | desc=%s%n",
                encomenda.getId(),
                encomenda.getNomeMorador(),
                encomenda.getApartamento(),
                encomenda.getDescricao()
        );
        System.out.printf(
                "Notificacao enviada ao morador %s (Apt %s): %s%n",
                encomenda.getNomeMorador(),
                encomenda.getApartamento(),
                encomenda.getDescricao()
        );
    }
}
