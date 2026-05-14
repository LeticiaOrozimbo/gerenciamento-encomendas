package com.projeto.domain.funcionario;

import jakarta.persistence.*;

/**
 * Entidade de domínio que representa um funcionário (porteiro) do condomínio.
 *
 * <p>Ao ser cadastrado, um {@link com.projeto.domain.usuario.Usuario} com perfil
 * {@link com.projeto.domain.usuario.Perfil#PORTEIRO} é criado automaticamente
 * para permitir o acesso ao sistema via JWT.</p>
 *
 * @author Equipe Projeto
 */
@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

