/**
 * Camada de <strong>Infraestrutura</strong> — adaptadores e frameworks externos.
 *
 * <p>Contém todas as implementações concretas que dependem de tecnologias externas:</p>
 * <ul>
 *   <li>{@code web} — Controllers REST (Spring MVC) e tratamento de erros</li>
 *   <li>{@code persistence} — Implementações de repositório com Spring Data JPA / H2 / PostgreSQL</li>
 *   <li>{@code messaging} — Publicadores e consumidores Spring Cloud Stream / RabbitMQ</li>
 *   <li>{@code security} — Configuração JWT, filtros e Spring Security</li>
 *   <li>{@code config} — Beans de configuração (OpenAPI, etc.)</li>
 * </ul>
 *
 * <p>Esta camada depende das camadas de domínio e aplicação, nunca o contrário.</p>
 */
package com.projeto.infrastructure;

