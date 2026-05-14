/**
 * Camada de <strong>Aplicação</strong> (Casos de Uso).
 *
 * <p>Orquestra as regras de negócio usando as entidades do domínio e as portas de saída.
 * Não depende diretamente de frameworks Web, JPA ou mensageria — apenas de interfaces.</p>
 *
 * <p>Serviços presentes:
 * <ul>
 *   <li>{@link com.projeto.application.encomenda.EncomendaService} — ciclo de vida das encomendas</li>
 *   <li>{@link com.projeto.application.morador.MoradorService} — cadastro de moradores</li>
 *   <li>{@link com.projeto.application.funcionario.FuncionarioService} — cadastro de funcionários</li>
 * </ul>
 */
package com.projeto.application;

