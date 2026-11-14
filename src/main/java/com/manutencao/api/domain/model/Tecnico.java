// File: src/main/java/com/manutencao/api/domain/model/Tecnico.java
package com.manutencao.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tecnico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tecnico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String especialidade;

    @Column(nullable = false)
    private Boolean ativo;
}