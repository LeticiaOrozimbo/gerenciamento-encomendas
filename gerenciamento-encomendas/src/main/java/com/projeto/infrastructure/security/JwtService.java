package com.projeto.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Serviço utilitário para geração e validação de tokens JWT.
 *
 * <p>Utiliza a biblioteca JJWT com algoritmo HS256 (HMAC-SHA256).
 * A chave secreta e o tempo de expiração são configurados via
 * {@code application.yml} ({@code jwt.secret} e {@code jwt.expiration}).</p>
 *
 * <p>O token inclui:
 * <ul>
 *   <li>{@code sub} — username do usuário</li>
 *   <li>{@code roles} — authorities do Spring Security</li>
 *   <li>{@code iat} / {@code exp} — data de emissão e expiração</li>
 * </ul>
 *
 * @author Equipe Projeto
 */
@Service
public class JwtService {

    @Value("${jwt.secret:mysecretkeyforjwtthatisatleast256bitslong12345}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String gerarToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairUsername(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public boolean validarToken(String token, UserDetails userDetails) {
        return extrairUsername(token).equals(userDetails.getUsername()) && !isTokenExpirado(token);
    }

    private boolean isTokenExpirado(String token) {
        return extrairClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extrairClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }
}

