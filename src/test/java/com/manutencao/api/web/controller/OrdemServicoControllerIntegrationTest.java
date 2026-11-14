package com.manutencao.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manutencao.api.domain.model.Equipamento;
import com.manutencao.api.domain.model.StatusEquipamento;
import com.manutencao.api.domain.repository.EquipamentoRepository;
import com.manutencao.api.domain.model.PrioridadeOS;
import com.manutencao.api.web.dto.OrdemServicoRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Utiliza H2 em memória via dependência Maven
class OrdemServicoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    private Long equipamentoId;

    @BeforeEach
    void setUp() {
        equipamentoRepository.deleteAll();
        Equipamento eq = Equipamento.builder()
                .nome("Torno CNC")
                .codigoTag("CNC-001")
                .setor("Usinagem")
                .status(StatusEquipamento.ATIVO)
                .build();
        equipamentoId = equipamentoRepository.save(eq).getId();
    }

    @Test
    void abrirOS_sucesso() throws Exception {
        OrdemServicoRequestDTO request = new OrdemServicoRequestDTO(
                equipamentoId,
                null,
                "Erro no eixo X",
                PrioridadeOS.ALTA
        );

        mockMvc.perform(post("/api/ordens-servico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ABERTA"))
                .andExpect(jsonPath("$.descricaoProblema").value("Erro no eixo X"));
    }

    @Test
    void abrirOS_falhaEquipamentoNaoEncontrado() throws Exception {
        OrdemServicoRequestDTO request = new OrdemServicoRequestDTO(
                999L,
                null,
                "Erro no eixo X",
                PrioridadeOS.ALTA
        );

        mockMvc.perform(post("/api/ordens-servico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}