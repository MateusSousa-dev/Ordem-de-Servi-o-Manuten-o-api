package com.manutencao.api.web.controller;

import com.manutencao.api.domain.model.Equipamento;
import com.manutencao.api.domain.service.EquipamentoService;
import com.manutencao.api.web.dto.EquipamentoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Equipamento>> listar() {
        return ResponseEntity.ok(equipamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Equipamento> salvar(@RequestBody EquipamentoDTO dto) {
        Equipamento novoEquipamento = equipamentoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEquipamento);
    }
}