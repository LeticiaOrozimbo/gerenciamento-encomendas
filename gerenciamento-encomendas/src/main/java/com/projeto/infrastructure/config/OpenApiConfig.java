package com.projeto.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do SpringDoc OpenAPI (Swagger UI).
 *
 * <p>Define metadados da API, esquema de segurança JWT Bearer e servidores disponíveis.
 * A documentação interativa fica disponível em {@code /swagger-ui/index.html}.</p>
 *
 * @author Equipe Projeto
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * Constrói o bean {@link OpenAPI} com todas as configurações da documentação.
     *
     * @return instância configurada de {@link OpenAPI}
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente de desenvolvimento (local)"),
                        new Server().url("http://api.encomendas.exemplo.com").description("Ambiente de produção")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, jwtSecurityScheme()));
    }

    /**
     * Metadados gerais da API.
     */
    private Info apiInfo() {
        return new Info()
                .title("API de Gerenciamento de Encomendas")
                .version("1.0.0")
                .description("""
                        ## Sistema de Gerenciamento de Encomendas para Condomínios
                        
                        API REST que permite:
                        - **Porteiros** receberem e baixarem encomendas na portaria
                        - **Moradores** confirmarem o recebimento da notificação de chegada
                        - Autenticação via **JWT** com controle de acesso por perfil (PORTEIRO / MORADOR)
                        - Integração com mensageria (**RabbitMQ**) para notificações assíncronas
                        
                        ### Fluxo principal
                        1. Porteiro registra chegada → `POST /encomendas`
                        2. Sistema publica evento na fila → morador é notificado
                        3. Morador confirma notificação → `PUT /encomendas/{id}/confirmar-notificacao`
                        4. Porteiro registra retirada → `PUT /encomendas/{id}/retirar`
                        
                        ### Autenticação
                        Faça login em `POST /auth/login` com `username` e `password` para obter o token JWT.
                        Inclua o token no header: `Authorization: Bearer <token>`
                        """)
                .contact(new Contact()
                        .name("Equipe Projeto")
                        .email("contato@projeto.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    /**
     * Esquema de segurança JWT Bearer Token para o Swagger UI.
     */
    private SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Informe o token JWT obtido em POST /auth/login");
    }
}

