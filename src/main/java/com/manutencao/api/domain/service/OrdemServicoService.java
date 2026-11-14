package com.manutencao.api.domain.service;

import com.manutencao.api.domain.model.Equipamento;
import com.manutencao.api.domain.model.OrdemServico;
import com.manutencao.api.domain.model.PrioridadeOS;
import com.manutencao.api.domain.model.StatusOS;
import com.manutencao.api.domain.model.Tecnico;
import com.manutencao.api.domain.repository.OrdemServicoRepository;
import com.manutencao.api.infrastructure.specification.OrdemServicoSpecification;
import com.manutencao.api.web.dto.ConcluirOsRequestDTO;
import com.manutencao.api.web.dto.MttrResponseDTO;
import com.manutencao.api.web.dto.OrdemServicoRequestDTO;
import com.manutencao.api.web.dto.OrdemServicoResponseDTO;
import com.manutencao.api.web.exception.BusinessRuleException;
import com.manutencao.api.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final EquipamentoService equipamentoService;
    private final TecnicoService tecnicoService;

    @Transactional
    public OrdemServicoResponseDTO abrirOS(OrdemServicoRequestDTO request) {
        Equipamento equipamento = equipamentoService.buscarPorId(request.equipamentoId());
        Tecnico tecnico = null;
        if (request.tecnicoId() != null) {
            tecnico = tecnicoService.buscarPorId(request.tecnicoId());
        }

        OrdemServico os = OrdemServico.builder()
                .equipamento(equipamento)
                .tecnico(tecnico)
                .descricaoProblema(request.descricaoProblema())
                .prioridade(request.prioridade())
                .status(StatusOS.ABERTA)
                .dataAbertura(LocalDateTime.now())
                .build();

        return OrdemServicoResponseDTO.fromEntity(ordemServicoRepository.save(os));
    }

    @Transactional
    public OrdemServicoResponseDTO iniciarAtendimento(Long osId, Long tecnicoId) {
        OrdemServico os = buscarPorId(osId);

        if (os.getStatus() != StatusOS.ABERTA) {
            throw new BusinessRuleException("Apenas Ordens de Serviço ABERTAS podem ser iniciadas.");
        }

        Tecnico tecnico = tecnicoService.buscarPorId(tecnicoId);
        if (!tecnico.getAtivo()) {
            throw new BusinessRuleException("Técnico selecionado não está ativo.");
        }

        os.setTecnico(tecnico);
        os.setStatus(StatusOS.EM_ANDAMENTO);
        os.setDataInicio(LocalDateTime.now());

        return OrdemServicoResponseDTO.fromEntity(ordemServicoRepository.save(os));
    }

    @Transactional
    public OrdemServicoResponseDTO concluirOS(Long osId, ConcluirOsRequestDTO request) {
        OrdemServico os = buscarPorId(osId);

        if (os.getStatus() != StatusOS.EM_ANDAMENTO) {
            throw new BusinessRuleException("Apenas Ordens de Serviço EM ANDAMENTO podem ser concluídas.");
        }

        if (request.descricaoSolucao() == null || request.descricaoSolucao().trim().isEmpty()) {
            throw new BusinessRuleException("A descrição da solução é obrigatória para concluir a Ordem de Serviço.");
        }

        os.setDescricaoSolucao(request.descricaoSolucao());
        os.setStatus(StatusOS.CONCLUIDA);
        os.setDataConclusao(LocalDateTime.now());

        return OrdemServicoResponseDTO.fromEntity(ordemServicoRepository.save(os));
    }

    @Transactional
    public void cancelarOS(Long osId) {
        OrdemServico os = buscarPorId(osId);

        if (os.getStatus() == StatusOS.CONCLUIDA || os.getStatus() == StatusOS.CANCELADA) {
            throw new BusinessRuleException("Não é possível cancelar uma Ordem de Serviço já Concluída ou Cancelada.");
        }

        os.setStatus(StatusOS.CANCELADA);
        ordemServicoRepository.save(os);
    }

    public Page<OrdemServicoResponseDTO> listar(StatusOS status, String setor, PrioridadeOS prioridade, Pageable pageable) {
        Specification<OrdemServico> spec = OrdemServicoSpecification.filterBy(status, setor, prioridade);
        return ordemServicoRepository.findAll(spec, pageable).map(OrdemServicoResponseDTO::fromEntity);
    }

    public OrdemServicoResponseDTO buscarDTOPorId(Long id) {
        return OrdemServicoResponseDTO.fromEntity(buscarPorId(id));
    }

    public OrdemServico buscarPorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com id: " + id));
    }

    public MttrResponseDTO calcularMttr(Long equipamentoId) {
        Equipamento equipamento = equipamentoService.buscarPorId(equipamentoId);
        List<OrdemServico> osConcluidas = ordemServicoRepository.findByEquipamentoIdAndStatus(equipamentoId, StatusOS.CONCLUIDA);

        if (osConcluidas.isEmpty()) {
            return new MttrResponseDTO(equipamentoId, equipamento.getNome(), equipamento.getCodigoTag(), 0, 0.0, "0 horas e 0 minutos");
        }

        long totalMinutos = 0;
        for (OrdemServico os : osConcluidas) {
            if (os.getDataInicio() != null && os.getDataConclusao() != null) {
                totalMinutos += Duration.between(os.getDataInicio(), os.getDataConclusao()).toMinutes();
            }
        }

        long mediaMinutos = totalMinutos / osConcluidas.size();
        double mttrEmHoras = Math.round((mediaMinutos / 60.0) * 100.0) / 100.0;
        
        long horasFormatadas = mediaMinutos / 60;
        long minutosFormatados = mediaMinutos % 60;
        String mttrFormatado = String.format("%d horas e %d minutos", horasFormatadas, minutosFormatados);

        return new MttrResponseDTO(
                equipamentoId,
                equipamento.getNome(),
                equipamento.getCodigoTag(),
                osConcluidas.size(),
                mttrEmHoras,
                mttrFormatado
        );
    }
}