/**
 * Camada de <strong>Domínio</strong> — coração da Clean Architecture.
 *
 * <p>Contém as entidades de negócio ({@link com.projeto.domain.encomenda.Encomenda},
 * {@link com.projeto.domain.morador.Morador}, {@link com.projeto.domain.funcionario.Funcionario},
 * {@link com.projeto.domain.usuario.Usuario}) e as interfaces de repositório (output ports).</p>
 *
 * <p><strong>Regra:</strong> esta camada não deve depender de nenhuma tecnologia externa
 * (Spring, JPA, etc.) além das anotações de mapeamento necessárias.</p>
 */
package com.projeto.domain;

