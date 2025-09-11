package com.datn.ailms.repositories.warehousetopology;

import com.datn.ailms.model.entities.topo_entities.BinRule;
import com.datn.ailms.model.entities.enums.RuleLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BinRuleRepository extends JpaRepository<BinRule, UUID> {
    /**
     * Lấy tất cả rule đã enable theo thứ tự priority tăng dần
     */
    List<BinRule> findAllByEnabledTrueOrderByPriorityAsc();

    /**
     * Lấy rules theo level (vd: chỉ tìm BIN-level rules)
     */
    List<BinRule> findAllByLevelAndEnabledTrueOrderByPriorityAsc(RuleLevel level);
}
