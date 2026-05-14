
package com.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Classe principal da aplicação Spring Boot — ponto de entrada do sistema.
 *
 * <p>Inicia o contexto do Spring com as configurações automáticas ({@code @SpringBootApplication})
 * e restringe o escaneamento de entidades JPA ao pacote {@code com.projeto.domain},
 * garantindo que apenas classes de domínio sejam gerenciadas pelo Hibernate.</p>
 *
 * <h2>Como executar</h2>
 * <pre>
 *   # Modo desenvolvimento (H2, sem RabbitMQ)
 *   mvn spring-boot:run
 *
 *   # Via Docker
 *   docker-compose up --build
 * </pre>
 *
 * <h2>Como executar os testes</h2>
 * <pre>
 *   mvn test                          # todos os testes
 *   mvn test -Dtest="*ServiceTest"    # apenas testes unitários
 *   mvn test -Dtest="*IntegrationTest"# apenas testes de integração
 *   mvn javadoc:javadoc               # gera Javadoc em target/site/apidocs
 * </pre>
 *
 * @author Equipe Projeto
 */
@SpringBootApplication
@EntityScan("com.projeto.domain")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
