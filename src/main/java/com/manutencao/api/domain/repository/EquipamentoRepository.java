package com.manutencao.api.domain.repository;

import com.manutencao.api.domain.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    boolean existsByCodigoTag(String codigoTag);
}