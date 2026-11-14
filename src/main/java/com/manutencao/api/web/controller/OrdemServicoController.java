package com.manutencao.api.web.controller;

import com.manutencao.api.domain.model.PrioridadeOS;
import com.manutencao.api.domain.model.StatusOS;
import com.manutencao.api.domain.service.OrdemServicoService;
import com.manutencao.api.web.dto.ConcluirOsRequestDTO;
import com.manutencao.api.web.dto.OrdemServicoRequestDTO;
import com.manutencao.api.web.dto.OrdemServicoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> abrirOS(@Valid @RequestBody OrdemServicoRequestDTO request) {
        OrdemServicoResponseDTO response = ordemServicoService.abrirOS(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrdemServicoResponseDTO>> listar(
            @RequestParam(required = false) StatusOS status,
            @RequestParam(required = false) String setor,
            @RequestParam(required = false) PrioridadeOS prioridade,
            Pageable pageable) {
        return ResponseEntity.ok(ordemServicoService.listar(status, setor, prioridade, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordemServicoService.buscarDTOPorId(id));
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<OrdemServicoResponseDTO> iniciarAtendimento(
            @PathVariable Long id,
            @RequestParam Long tecnicoId) {
        return ResponseEntity.ok(ordemServicoService.iniciarAtendimento(id, tecnicoId));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<OrdemServicoResponseDTO> concluirOS(
            @PathVariable Long id,
            @Valid @RequestBody ConcluirOsRequestDTO request) {
        return ResponseEntity.ok(ordemServicoService.concluirOS(id, request));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarOS(@PathVariable Long id) {
        ordemServicoService.cancelarOS(id);
        return ResponseEntity.noContent().build();
    }
}