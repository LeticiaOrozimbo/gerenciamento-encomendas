package com.projeto.application.encomenda;

import com.projeto.domain.encomenda.Encomenda;

/**
 * Porta de saída (output port) para publicação de eventos de encomenda em fila de mensageria.
 *
 * <p>Seguindo a Clean Architecture, este contrato pertence à camada de aplicação.
 * As implementações concretas ficam na infraestrutura:
 * <ul>
 *   <li>{@code EncomendaStreamPublisher} — usa RabbitMQ via Spring Cloud Stream (perfil {@code prod})</li>
 *   <li>{@code EncomendaDevPublisher} — imprime no console (perfil {@code dev})</li>
 *   <li>{@code EncomendaTestPublisher} — sem efeito colateral (perfil {@code test})</li>
 * </ul>
 *
 * @author Equipe Projeto
 */
public interface EncomendaPublisher {

    /**
     * Publica o evento de encomenda recebida para processamento assíncrono.
     *
     * @param encomenda entidade persistida com id preenchido
     */
    void publicar(Encomenda encomenda);
}

