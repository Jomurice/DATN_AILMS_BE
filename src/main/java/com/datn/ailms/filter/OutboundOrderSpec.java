package com.datn.ailms.filter;

import com.datn.ailms.model.dto.OutboundOrderFilter;
import com.datn.ailms.model.entities.enums.OrderStatus;
import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OutboundOrderSpec {

    public static Specification<OutboundOrder> filter (OutboundOrderFilter f){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.notEqual(root.get("status"), OrderStatus.DRAFT.name())
            );

            //status
            if(f.getStatus() != null && !f.getStatus().isBlank()){
                predicates.add(cb.equal(root.get("status"), f.getStatus()));
            }

            //search
            if(f.getSearch() != null &&  !f.getSearch().isBlank()){
                predicates.add(
                        cb.like(
                                cb.lower(root.get("code")),
                                "%" + f.getSearch().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
