package com.manutencao.api.domain.repository;

import com.manutencao.api.domain.model.OrdemServico;
import com.manutencao.api.domain.model.StatusOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long>, JpaSpecificationExecutor<OrdemServico> {
    List<OrdemServico> findByEquipamentoIdAndStatus(Long equipamentoId, StatusOS status);
}