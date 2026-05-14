package com.projeto.infrastructure.web;

import com.projeto.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST responsável pela autenticação via JWT.
 *
 * <p>Endpoints públicos — não requerem token para acesso.
 * O token retornado deve ser incluído no header {@code Authorization: Bearer <token>}
 * em todas as chamadas autenticadas.</p>
 *
 * @author Equipe Projeto
 */
@Tag(name = "Autenticação", description = "Login e obtenção de token JWT")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Autentica um usuário e retorna um token JWT.
     *
     * @param credentials mapa com campos {@code username} e {@code password}
     * @return mapa com o campo {@code token} contendo o JWT gerado, ou erro 401 se inválido
     */
    @Operation(summary = "Login — obter token JWT",
               description = "Informe username e password. O token retornado expira em 24h.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Token gerado com sucesso"),
                   @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
               })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.get("username"),
                            credentials.get("password")
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Credenciais inválidas: usuário ou senha incorretos"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Falha na autenticação: " + e.getMessage()));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.get("username"));
        String token = jwtService.gerarToken(userDetails);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

