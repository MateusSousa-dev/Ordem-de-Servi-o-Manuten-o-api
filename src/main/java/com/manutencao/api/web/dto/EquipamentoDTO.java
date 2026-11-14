package com.manutencao.api.web.dto;

import com.manutencao.api.domain.model.Equipamento;

public record EquipamentoDTO(
    Long id,
    String nome,
    String codigoTag,
    String setor,
    String status
) {
    public static EquipamentoDTO fromEntity(Equipamento equipamento) {
        if (equipamento == null) {
            return null;
        }
        return new EquipamentoDTO(
            equipamento.getId(),
            equipamento.getNome(),
            equipamento.getCodigoTag(),
            equipamento.getSetor(),
            equipamento.getStatus() != null ? equipamento.getStatus().name() : null
        );
    }
}