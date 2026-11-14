package com.manutencao.api.web.controller;

import com.manutencao.api.domain.service.OrdemServicoService;
import com.manutencao.api.web.dto.MttrResponseDTO; // Ajuste o import se o DTO estiver em outro pacote
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/indicadores")
public class IndicadoresController {

    private final OrdemServicoService ordemServicoService;

    // Construtor manual para injeção de dependência (substitui o @RequiredArgsConstructor)
    public IndicadoresController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<String> obterResumo() {
        return ResponseEntity.ok("Módulo de Indicadores Ativo");
    }

    @GetMapping("/mttr/{equipamentoId}")
    public ResponseEntity<MttrResponseDTO> calcularMttr(@PathVariable Long equipamentoId) {
        return ResponseEntity.ok(ordemServicoService.calcularMttr(equipamentoId));
    }
}