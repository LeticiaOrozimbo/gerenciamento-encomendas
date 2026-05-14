package com.projeto.infrastructure.web;

import com.projeto.application.encomenda.EncomendaService;
import com.projeto.domain.encomenda.Encomenda;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações do ciclo de vida das encomendas.
 *
 * <p>Expõe endpoints para recebimento, retirada, confirmação de notificação
 * e consulta de encomendas. Acesso restrito por perfil:
 * <ul>
 *   <li>Recebimento e retirada: apenas {@code PORTEIRO}</li>
 *   <li>Confirmação de notificação: {@code MORADOR} ou {@code PORTEIRO}</li>
 *   <li>Consultas: qualquer usuário autenticado</li>
 * </ul>
 *
 * @author Equipe Projeto
 */
@Tag(name = "Encomendas", description = "Gerenciamento do ciclo de vida das encomendas na portaria")
@RestController
@RequestMapping("/encomendas")
public class EncomendaController {

    private final EncomendaService encomendaService;

    public EncomendaController(EncomendaService encomendaService) {
        this.encomendaService = encomendaService;
    }

    /**
     * Registra a chegada de uma nova encomenda.
     * Requer perfil PORTEIRO.
     */
    @Operation(summary = "Registrar nova encomenda",
               description = "Porteiro registra chegada de encomenda. Status inicial: RECEBIDA. Dispara notificação via mensageria.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Encomenda registrada com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Apartamento não possui morador cadastrado"),
                   @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil PORTEIRO")
               })
    @PostMapping
    public Encomenda receber(@RequestBody Encomenda encomenda) {
        return encomendaService.receberEncomenda(encomenda);
    }

    /**
     * Registra a retirada de uma encomenda pelo morador.
     * Requer perfil PORTEIRO.
     */
    @Operation(summary = "Registrar retirada da encomenda",
               description = "Porteiro dá baixa quando morador retira o pacote. Status atualizado para RETIRADA.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Retirada registrada com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Encomenda não encontrada"),
                   @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil PORTEIRO")
               })
    @PutMapping("/{id}/retirar")
    public Encomenda retirar(@Parameter(description = "ID da encomenda") @PathVariable Long id) {
        return encomendaService.retirarEncomenda(id);
    }

    /**
     * Confirma que o morador recebeu a notificação de chegada.
     * Requer perfil MORADOR ou PORTEIRO.
     */
    @Operation(summary = "Confirmar notificação",
               description = "Morador confirma que recebeu a notificação de chegada da encomenda.")
    @PutMapping("/{id}/confirmar-notificacao")
    public Encomenda confirmarNotificacao(@Parameter(description = "ID da encomenda") @PathVariable Long id) {
        return encomendaService.confirmarNotificacao(id);
    }

    /**
     * Lista todas as encomendas registradas.
     */
    @Operation(summary = "Listar todas as encomendas")
    @GetMapping
    public List<Encomenda> listar() {
        return encomendaService.listarEncomendas();
    }

    /**
     * Busca uma encomenda pelo ID.
     */
    @Operation(summary = "Buscar encomenda por ID")
    @GetMapping("/{id}")
    public Encomenda buscar(@Parameter(description = "ID da encomenda") @PathVariable Long id) {
        return encomendaService.buscarEncomenda(id);
    }
}



