package com.manutencao.api.web.dto;

import com.manutencao.api.domain.model.Tecnico;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TecnicoDTO(
        Long id,
        @NotBlank(message = "O nome é obrigatório") String nome,
        @NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") String email,
        @NotBlank(message = "A especialidade é obrigatória") String especialidade,
        @NotNull(message = "O status ativo é obrigatório") Boolean ativo
) {
    public static TecnicoDTO fromEntity(Tecnico tecnico) {
        return new TecnicoDTO(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getEmail(),
                tecnico.getEspecialidade(),
                tecnico.getAtivo()
        );
    }
}