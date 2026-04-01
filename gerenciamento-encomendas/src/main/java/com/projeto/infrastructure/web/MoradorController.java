package com.projeto.infrastructure.web;

import com.projeto.application.morador.MoradorService;
import com.projeto.domain.morador.Morador;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/moradores")
public class MoradorController {

    private final MoradorService moradorService;

    public MoradorController(MoradorService moradorService) {
        this.moradorService = moradorService;
    }

    @PostMapping
    public Morador cadastrar(@RequestBody Morador morador) {
        return moradorService.cadastrarMorador(morador);
    }

    @PutMapping("/{id}")
    public Morador atualizar(@PathVariable Long id, @RequestBody Morador dados) {
        return moradorService.atualizarMorador(id, dados);
    }

    @GetMapping
    public List<Morador> listar() {
        return moradorService.listarMoradores();
    }

    @GetMapping("/apartamento/{apt}")
    public Morador buscarPorApartamento(@PathVariable String apt) {
        return moradorService.buscarPorApartamento(apt);
    }
}

