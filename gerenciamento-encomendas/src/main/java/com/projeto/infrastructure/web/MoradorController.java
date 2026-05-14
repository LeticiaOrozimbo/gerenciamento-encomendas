package com.projeto.infrastructure.web;

import com.projeto.application.morador.MoradorService;
import com.projeto.domain.morador.Morador;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para cadastro e consulta de moradores.
 *
 * <p>O cadastro ({@code POST /moradores}) é público — não exige autenticação.
 * As demais operações requerem token JWT válido.</p>
 *
 * @author Equipe Projeto
 */
@Tag(name = "Moradores", description = "Cadastro e manutenção dos moradores do condomínio")
@RestController
@RequestMapping("/moradores")
public class MoradorController {

    private final MoradorService moradorService;

    public MoradorController(MoradorService moradorService) {
        this.moradorService = moradorService;
    }

    /**
     * Cadastra um novo morador. Endpoint público.
     * Se {@code login} e {@code senha} forem informados, cria credenciais de acesso.
     */
    @Operation(summary = "Cadastrar morador",
               description = "Endpoint público. Informe login e senha para criar acesso ao sistema.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Morador cadastrado com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Dados inválidos")
               })
    @PostMapping
    public Morador cadastrar(@RequestBody Morador morador) {
        return moradorService.cadastrarMorador(morador);
    }

    /**
     * Atualiza os dados de um morador existente. Requer autenticação.
     */
    @Operation(summary = "Atualizar dados do morador")
    @PutMapping("/{id}")
    public Morador atualizar(@Parameter(description = "ID do morador") @PathVariable Long id,
                             @RequestBody Morador dados) {
        return moradorService.atualizarMorador(id, dados);
    }

    /**
     * Lista todos os moradores cadastrados.
     */
    @Operation(summary = "Listar todos os moradores")
    @GetMapping
    public List<Morador> listar() {
        return moradorService.listarMoradores();
    }

    /**
     * Busca morador pelo número do apartamento.
     */
    @Operation(summary = "Buscar morador por apartamento")
    @GetMapping("/apartamento/{apt}")
    public Morador buscarPorApartamento(
            @Parameter(description = "Número do apartamento") @PathVariable String apt) {
        return moradorService.buscarPorApartamento(apt);
    }
}

