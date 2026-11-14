package com.manutencao.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "codigo_tag", nullable = false, unique = true)
    private String codigoTag;

    @Column(nullable = false)
    private String setor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEquipamento status;
}