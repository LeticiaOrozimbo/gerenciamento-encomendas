package com.projeto.domain.encomenda;

import jakarta.persistence.*;


@Entity
@Table(name = "encomendas")
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeMorador;

    @Column(nullable = false)
    private String apartamento;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEncomenda status;

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

