package com.projeto.domain.encomenda;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Entidade de domínio que representa uma encomenda recebida na portaria do condomínio.
 *
 * <p>Ciclo de vida do status:
 * <ol>
 *   <li>{@link StatusEncomenda#RECEBIDA} – encomenda chegou na portaria</li>
 *   <li>{@link StatusEncomenda#NOTIFICADA} – morador foi notificado</li>
 *   <li>{@link StatusEncomenda#RETIRADA} – morador retirou o pacote</li>
 * </ol>
 *
 * @author Equipe Projeto
 */
@Entity
@Table(name = "encomendas")
public class Encomenda {

    /** Identificador único gerado automaticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome completo do morador destinatário. */
    @Column(nullable = false)
    private String nomeMorador;

    /** Número do apartamento do destinatário. */
    @Column(nullable = false)
    private String apartamento;

    /** Descrição resumida do conteúdo ou remetente da encomenda. */
    @Column(nullable = false)
    private String descricao;

    /** Estado atual da encomenda no fluxo de entrega. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEncomenda status;

    /** Indica se o morador confirmou o recebimento da notificação de chegada. */
    private boolean notificacaoConfirmada = false;

    public Long getId() { return id; }

    public String getNomeMorador() { return nomeMorador; }
    public void setNomeMorador(String nomeMorador) { this.nomeMorador = nomeMorador; }

    public String getApartamento() { return apartamento; }
    public void setApartamento(String apartamento) { this.apartamento = apartamento; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusEncomenda getStatus() { return status; }
    public void setStatus(StatusEncomenda status) { this.status = status; }

    public boolean isNotificacaoConfirmada() { return notificacaoConfirmada; }
    public void setNotificacaoConfirmada(boolean notificacaoConfirmada) {
        this.notificacaoConfirmada = notificacaoConfirmada;
    }
}

