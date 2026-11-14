package com.manutencao.api.domain.service;

import com.manutencao.api.domain.model.*;
import com.manutencao.api.domain.repository.OrdemServicoRepository;
import com.manutencao.api.web.dto.ConcluirOsRequestDTO;
import com.manutencao.api.web.dto.MttrResponseDTO;
import com.manutencao.api.web.dto.OrdemServicoResponseDTO;
import com.manutencao.api.web.exception.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private EquipamentoService equipamentoService;

    @Mock
    private TecnicoService tecnicoService;

    @InjectMocks
    private OrdemServicoService ordemServicoService;

    private Equipamento equipamento;
    private Tecnico tecnico;
    private OrdemServico osAberta;
    private OrdemServico osEmAndamento;

    @BeforeEach
    void setUp() {
        equipamento = new Equipamento(1L, "Compressor 1", "COMP-001", "Manutenção", StatusEquipamento.ATIVO);
        tecnico = new Tecnico(1L, "João Silva", "joao@email.com", "Mecânica", true);
        
        osAberta = OrdemServico.builder()
                .id(1L)
                .equipamento(equipamento)
                .descricaoProblema("Vazamento")
                .prioridade(PrioridadeOS.ALTA)
                .status(StatusOS.ABERTA)
                .dataAbertura(LocalDateTime.now())
                .build();

        osEmAndamento = OrdemServico.builder()
                .id(2L)
                .equipamento(equipamento)
                .tecnico(tecnico)
                .descricaoProblema("Vazamento")
                .prioridade(PrioridadeOS.ALTA)
                .status(StatusOS.EM_ANDAMENTO)
                .dataAbertura(LocalDateTime.now().minusHours(2))
                .dataInicio(LocalDateTime.now().minusHours(1))
                .build();
    }

    @Test
    void iniciarAtendimento_sucesso() {
        when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(osAberta));
        when(tecnicoService.buscarPorId(1L)).thenReturn(tecnico);
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenAnswer(i -> i.getArguments()[0]);

        OrdemServicoResponseDTO response = ordemServicoService.iniciarAtendimento(1L, 1L);

        assertEquals(StatusOS.EM_ANDAMENTO, response.status());
        assertNotNull(response.dataInicio());
        assertEquals("João Silva", response.tecnico().nome());
    }

    @Test
    void iniciarAtendimento_falhaStatusInvalido() {
        when(ordemServicoRepository.findById(2L)).thenReturn(Optional.of(osEmAndamento));

        assertThrows(BusinessRuleException.class, () -> ordemServicoService.iniciarAtendimento(2L, 1L));
    }

    @Test
    void concluirOS_sucesso() {
        ConcluirOsRequestDTO request = new ConcluirOsRequestDTO("Peça trocada");
        when(ordemServicoRepository.findById(2L)).thenReturn(Optional.of(osEmAndamento));
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenAnswer(i -> i.getArguments()[0]);

        OrdemServicoResponseDTO response = ordemServicoService.concluirOS(2L, request);

        assertEquals(StatusOS.CONCLUIDA, response.status());
        assertEquals("Peça trocada", response.descricaoSolucao());
        assertNotNull(response.dataConclusao());
    }

    @Test
    void concluirOS_falhaSemSolucao() {
        ConcluirOsRequestDTO request = new ConcluirOsRequestDTO("");
        when(ordemServicoRepository.findById(2L)).thenReturn(Optional.of(osEmAndamento));

        assertThrows(BusinessRuleException.class, () -> ordemServicoService.concluirOS(2L, request));
    }

    @Test
    void calcularMttr_sucesso() {
        OrdemServico os1 = OrdemServico.builder()
                .dataInicio(LocalDateTime.of(2024, 1, 1, 10, 0))
                .dataConclusao(LocalDateTime.of(2024, 1, 1, 12, 30)) // 150 min
                .build();
        OrdemServico os2 = OrdemServico.builder()
                .dataInicio(LocalDateTime.of(2024, 1, 2, 10, 0))
                .dataConclusao(LocalDateTime.of(2024, 1, 2, 14, 0)) // 240 min
                .build();
        // Média = 195 minutos (3 horas e 15 min) = 3.25 horas

        when(equipamentoService.buscarPorId(1L)).thenReturn(equipamento);
        when(ordemServicoRepository.findByEquipamentoIdAndStatus(1L, StatusOS.CONCLUIDA)).thenReturn(List.of(os1, os2));

        MttrResponseDTO response = ordemServicoService.calcularMttr(1L);

        assertEquals(2, response.totalOsConcluidas());
        assertEquals(3.25, response.mttrEmHoras());
        assertEquals("3 horas e 15 minutos", response.mttrFormatado());
    }
}