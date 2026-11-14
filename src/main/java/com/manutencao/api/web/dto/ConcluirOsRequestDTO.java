package com.manutencao.api.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ConcluirOsRequestDTO(
        @NotBlank(message = "A descrição da solução é obrigatória para conclusão") String descricaoSolucao
) {
}