package com.manutencao.api.domain.service;

import com.manutencao.api.domain.model.Tecnico;
import com.manutencao.api.domain.repository.TecnicoRepository;
import com.manutencao.api.web.dto.TecnicoDTO;
import com.manutencao.api.web.exception.BusinessRuleException;
import com.manutencao.api.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TecnicoService {
    private final TecnicoRepository tecnicoRepository;

    @Transactional
    public TecnicoDTO criar(TecnicoDTO dto) {
        if (tecnicoRepository.existsByEmail(dto.email())) {
            throw new BusinessRuleException("Técnico com email " + dto.email() + " já existe.");
        }
        
        Tecnico tecnico = Tecnico.builder()
                .nome(dto.nome())
                .email(dto.email())
                .especialidade(dto.especialidade())
                .ativo(dto.ativo())
                .build();
                
        return TecnicoDTO.fromEntity(tecnicoRepository.save(tecnico));
    }

    public Tecnico buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado com id: " + id));
    }
}