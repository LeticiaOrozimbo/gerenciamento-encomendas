package com.projeto.infrastructure.web;

import com.projeto.application.encomenda.EncomendaService;
import com.projeto.domain.encomenda.Encomenda;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/encomendas")
public class EncomendaController {

    private final EncomendaService encomendaService;

    public EncomendaController(EncomendaService encomendaService) {
        this.encomendaService = encomendaService;
    }

    @PostMapping
    public Encomenda receber(@RequestBody Encomenda encomenda) {
        return encomendaService.receberEncomenda(encomenda);
    }

    @PutMapping("/{id}/retirar")
    public Encomenda retirar(@PathVariable Long id) {
        return encomendaService.retirarEncomenda(id);
    }

    @PutMapping("/{id}/confirmar-notificacao")
    public Encomenda confirmarNotificacao(@PathVariable Long id) {
        return encomendaService.confirmarNotificacao(id);
    }

    @GetMapping
    public List<Encomenda> listar() {
        return encomendaService.listarEncomendas();
    }

    @GetMapping("/{id}")
    public Encomenda buscar(@PathVariable Long id) {
        return encomendaService.buscarEncomenda(id);
    }
}

