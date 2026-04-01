package com.projeto.application.morador;

import com.projeto.domain.morador.Morador;
import com.projeto.domain.morador.MoradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MoradorService {

    private final MoradorRepository moradorRepository;

    public MoradorService(MoradorRepository moradorRepository) {
        this.moradorRepository = moradorRepository;
    }

    public Morador cadastrarMorador(Morador morador) {
        return moradorRepository.salvar(morador);
    }

    public Morador atualizarMorador(Long id, Morador dados) {
        Morador morador = moradorRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Morador não encontrado: " + id));

        morador.setNome(dados.getNome());
        morador.setTelefone(dados.getTelefone());
        morador.setApartamento(dados.getApartamento());
        return moradorRepository.salvar(morador);
    }

    public List<Morador> listarMoradores() {
        return moradorRepository.listarTodos();
    }

    public Morador buscarPorApartamento(String apartamento) {
        return moradorRepository.buscarPorApartamento(apartamento)
                .orElseThrow(() -> new RuntimeException("Morador não encontrado no apartamento: " + apartamento));
    }
}

