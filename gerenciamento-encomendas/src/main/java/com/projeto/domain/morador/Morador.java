package com.projeto.domain.morador;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


/**
 * Entidade de domínio que representa um morador do condomínio.
 *
 * <p>O morador é o destinatário das encomendas e pode opcionalmente ter um
 * login/senha para acesso ao sistema (campos {@code @Transient} usados apenas
 * no momento do cadastro para criar o {@link com.projeto.domain.usuario.Usuario}).</p>
 *
 * @author Equipe Projeto
 */
@Entity
@Table(name = "moradores")
public class Morador {

    /** Identificador único gerado automaticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome completo do morador. */
    @Column(nullable = false)
    private String nome;

    /** Telefone de contato do morador. */
    @Column(nullable = false)
    private String telefone;

    /** Número do apartamento; deve ser único no condomínio. */
    @Column(nullable = false, unique = true)
    private String apartamento;

    /**
     * Login desejado para acesso ao sistema.
     * Campo transitório — não é persistido na tabela de moradores.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String login;

    /**
     * Senha em texto plano fornecida no cadastro.
     * Campo transitório — não é persistido; a senha codificada vai para {@link com.projeto.domain.usuario.Usuario}.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String senha;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getApartamento() {
        return apartamento;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

