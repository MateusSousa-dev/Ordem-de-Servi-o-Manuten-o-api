package com.manutencao.api.infrastructure.specification;

import com.manutencao.api.domain.model.OrdemServico;
import com.manutencao.api.domain.model.PrioridadeOS;
import com.manutencao.api.domain.model.StatusOS;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrdemServicoSpecification {
    public static Specification<OrdemServico> filterBy(StatusOS status, String setor, PrioridadeOS prioridade) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (setor != null && !setor.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.join("equipamento").get("setor"), setor));
            }

            if (prioridade != null) {
                predicates.add(criteriaBuilder.equal(root.get("prioridade"), prioridade));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}