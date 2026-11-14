package com.manutencao.api.web.dto;

import com.manutencao.api.domain.model.OrdemServico;
import com.manutencao.api.domain.model.PrioridadeOS;
import com.manutencao.api.domain.model.StatusOS;

import java.time.LocalDateTime;

public record OrdemServicoResponseDTO(
        Long id,
        EquipamentoDTO equipamento,
        TecnicoDTO tecnico,
        String descricaoProblema,
        String descricaoSolucao,
        PrioridadeOS prioridade,
        StatusOS status,
        LocalDateTime dataAbertura,
        LocalDateTime dataInicio,
        LocalDateTime dataConclusao
) {
    public static OrdemServicoResponseDTO fromEntity(OrdemServico os) {
        return new OrdemServicoResponseDTO(
                os.getId(),
                EquipamentoDTO.fromEntity(os.getEquipamento()),
                os.getTecnico() != null ? TecnicoDTO.fromEntity(os.getTecnico()) : null,
                os.getDescricaoProblema(),
                os.getDescricaoSolucao(),
                os.getPrioridade(),
                os.getStatus(),
                os.getDataAbertura(),
                os.getDataInicio(),
                os.getDataConclusao()
        );
    }
}