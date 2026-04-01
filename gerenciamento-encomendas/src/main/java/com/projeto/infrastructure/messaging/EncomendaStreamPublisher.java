package com.projeto.infrastructure.messaging;

import com.projeto.application.encomenda.EncomendaPublisher;
import com.projeto.domain.encomenda.Encomenda;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class EncomendaStreamPublisher implements EncomendaPublisher {

    private static final String CANAL_ENTRADA = "encomendas-recebidas";

    private final StreamBridge streamBridge;

    public EncomendaStreamPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void publicar(Encomenda encomenda) {
        streamBridge.send(CANAL_ENTRADA, encomenda);
    }
}

