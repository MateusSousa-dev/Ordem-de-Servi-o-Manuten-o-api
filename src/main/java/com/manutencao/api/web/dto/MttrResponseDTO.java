package com.manutencao.api.web.dto;

public record MttrResponseDTO(
        Long equipamentoId,
        String nomeEquipamento,
        String codigoTag,
        long totalOsConcluidas,
        double mttrEmHoras,
        String mttrFormatado
) {
}