package com.manutencao.api.domain.repository;

import com.manutencao.api.domain.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    boolean existsByEmail(String email);
}