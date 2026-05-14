package com.projeto.infrastructure.web;

import com.projeto.application.funcionario.FuncionarioService;
import com.projeto.domain.funcionario.Funcionario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para cadastro e manutenção de funcionários (porteiros).
 *
 * <p>O cadastro ({@code POST /funcionarios}) é público.
 * As demais operações exigem autenticação JWT com perfil {@code PORTEIRO}.</p>
 *
 * @author Equipe Projeto
 */
@Tag(name = "Funcionários", description = "Cadastro e manutenção dos funcionários (porteiros) do condomínio")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    /**
     * Cadastra um novo funcionário. Endpoint público.
     * Cria automaticamente credenciais com perfil PORTEIRO.
     */
    @Operation(summary = "Cadastrar funcionário",
               description = "Endpoint público. Cria o funcionário e suas credenciais de acesso com perfil PORTEIRO.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Funcionário cadastrado com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Dados inválidos")
               })
    @PostMapping
    public ResponseEntity<Funcionario> cadastrar(@RequestBody Funcionario funcionario) {
        return ResponseEntity.ok(service.cadastrar(funcionario));
    }

    /** Lista todos os funcionários cadastrados. */
    @Operation(summary = "Listar funcionários")
    @GetMapping
    public ResponseEntity<List<Funcionario>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /** Busca funcionário pelo ID. */
    @Operation(summary = "Buscar funcionário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscar(
            @Parameter(description = "ID do funcionário") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /** Atualiza dados de um funcionário. */
    @Operation(summary = "Atualizar funcionário")
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(
            @Parameter(description = "ID do funcionário") @PathVariable Long id,
            @RequestBody Funcionario dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    /** Remove um funcionário do sistema. */
    @Operation(summary = "Remover funcionário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do funcionário") @PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}


