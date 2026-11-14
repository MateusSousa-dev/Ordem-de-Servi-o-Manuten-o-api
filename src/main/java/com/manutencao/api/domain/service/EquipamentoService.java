package com.manutencao.api.domain.service;

import com.manutencao.api.domain.model.Equipamento;
import com.manutencao.api.domain.model.StatusEquipamento;
import com.manutencao.api.domain.repository.EquipamentoRepository;
import com.manutencao.api.web.dto.EquipamentoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<Equipamento> listarTodos() {
        return equipamentoRepository.findAll();
    }

    public Equipamento buscarPorId(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado com id: " + id));
    }

    @Transactional
    public Equipamento salvar(EquipamentoDTO dto) {
        Equipamento equipamento = new Equipamento();
        equipamento.setNome(dto.nome());
        equipamento.setCodigoTag(dto.codigoTag());
        equipamento.setSetor(dto.setor());
        
        // Converte com segurança para ATIVO/INATIVO, usando ATIVO como padrão
        if (dto.status() != null && !dto.status().isBlank()) {
            try {
                equipamento.setStatus(StatusEquipamento.valueOf(dto.status().toUpperCase().trim()));
            } catch (IllegalArgumentException e) {
                equipamento.setStatus(StatusEquipamento.ATIVO);
            }
        } else {
            equipamento.setStatus(StatusEquipamento.ATIVO);
        }
        
        return equipamentoRepository.save(equipamento);
    }
}