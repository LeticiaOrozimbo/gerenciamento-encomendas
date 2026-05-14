package com.projeto.domain.encomenda;

/**
 * Representa os possíveis estados de uma {@link Encomenda} no seu ciclo de vida.
 *
 * @author Equipe Projeto
 */
public enum StatusEncomenda {

    /** Encomenda chegou na portaria e foi registrada pelo porteiro. */
    RECEBIDA,

    /** Morador foi notificado sobre a chegada da encomenda. */
    NOTIFICADA,

    /** Morador retirou a encomenda na portaria. */
    RETIRADA
}

