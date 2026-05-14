package com.projeto.domain.usuario;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidade de segurança que implementa {@link UserDetails} do Spring Security.
 *
 * <p>Representa credenciais de acesso ao sistema. Cada {@link com.projeto.domain.morador.Morador}
 * ou {@link com.projeto.domain.funcionario.Funcionario} pode ter um {@code Usuario} associado
 * com o perfil adequado ({@link Perfil#MORADOR} ou {@link Perfil#PORTEIRO}).</p>
 *
 * <p>As autoridades são derivadas do {@link Perfil} com o prefixo {@code ROLE_}.</p>
 *
 * @author Equipe Projeto
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    public Long getId() { return id; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public Perfil getPerfil() { return perfil; }

    public void setPerfil(Perfil perfil) { this.perfil = perfil; }

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

