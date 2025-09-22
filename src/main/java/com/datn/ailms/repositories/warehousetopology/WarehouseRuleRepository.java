package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.topo_entities.WarehouseRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarehouseRuleRepository extends JpaRepository<WarehouseRule, UUID> {
    List<WarehouseRule> findAllByEnabledTrueOrderByPriorityAsc();
}
