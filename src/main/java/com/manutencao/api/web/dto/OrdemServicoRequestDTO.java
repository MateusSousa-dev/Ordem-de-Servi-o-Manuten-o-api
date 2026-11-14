package com.manutencao.api.web.dto;

import com.manutencao.api.domain.model.PrioridadeOS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdemServicoRequestDTO(
        @NotNull(message = "O ID do equipamento é obrigatório") Long equipamentoId,
        Long tecnicoId,
        @NotBlank(message = "A descrição do problema é obrigatória") String descricaoProblema,
        @NotNull(message = "A prioridade é obrigatória") PrioridadeOS prioridade
) {
}