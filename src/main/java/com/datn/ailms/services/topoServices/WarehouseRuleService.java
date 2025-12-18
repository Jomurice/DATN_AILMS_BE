package com.datn.ailms.services.topoServices;

import com.datn.ailms.mapper.WarehouseRuleMapper;
import com.datn.ailms.model.dto.request.warehouse_request.WarehouseRuleRequestDto;
import com.datn.ailms.model.dto.response.warehouse_response.WarehouseRuleResponseDto;
import com.datn.ailms.model.entities.topo_entities.Warehouse;
import com.datn.ailms.model.entities.topo_entities.WarehouseRule;
import com.datn.ailms.repositories.warehousetopology.WarehouseRepository;
import com.datn.ailms.repositories.warehousetopology.WarehouseRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class WarehouseRuleService {
    private final WarehouseRuleRepository ruleRepo;
    private final WarehouseRepository warehouseRepo;
    private final WarehouseRuleMapper mapper; // dùng để map entity -> response DTO

    // ---------------- existing rule matching (kept) ----------------
    /**
     * Tìm warehouse bằng quy tắc: load tất cả rule enabled theo priority,
     * duyệt từ đầu -> first match wins.
     */
    public Optional<Warehouse> findWarehouseForSerialOptional(String serial) {
        if (serial == null) return Optional.empty();

        List<WarehouseRule> rules = ruleRepo.findAllByEnabledTrueOrderByPriorityAsc();
        for (WarehouseRule rule : rules) {
            String p = rule.getPattern();
            if (p == null || p.isBlank()) continue;
            try {
                Pattern pattern = Pattern.compile(p);
                if (pattern.matcher(serial).find()) {
                    return Optional.ofNullable(rule.getWarehouse());
                }
            } catch (Exception ex) {
                // log cảnh báo regex lỗi (nên logging thực tế)
            }
        }
        return Optional.empty();
    }

    public Warehouse findWarehouseForSerial(String serial) {
        return findWarehouseForSerialOptional(serial)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No warehouse rule matched for serial: " + serial));
    }

    // ---------------- CRUD theo DTO (mới, dùng trong controller) ----------------

    @Transactional
    public WarehouseRuleResponseDto createRule(WarehouseRuleRequestDto req) {
        if (req == null) throw new IllegalArgumentException("Request is null");

        // load warehouse
        Warehouse wh = warehouseRepo.findById(req.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found: " + req.getWarehouseId()));

        WarehouseRule rule = WarehouseRule.builder()
                .pattern(req.getPattern())
                .level(req.getLevel())
                .warehouse(wh)
                .priority(req.getPriority() != null ? req.getPriority() : 100)
                .enabled(req.getEnabled() != null ? req.getEnabled() : Boolean.TRUE)
                .build();

        WarehouseRule saved = ruleRepo.save(rule);
        return mapper.toResponse(saved);
    }

    @Transactional
    public WarehouseRuleResponseDto updateRule(UUID id, WarehouseRuleRequestDto req) {
        WarehouseRule rule = ruleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found: " + id));

        // chỉ update nếu trường trong req khác null
        if (req.getPattern() != null) rule.setPattern(req.getPattern());
        if (req.getLevel() != null) rule.setLevel(req.getLevel());
        if (req.getPriority() != null) rule.setPriority(req.getPriority());
        if (req.getEnabled() != null) rule.setEnabled(req.getEnabled());

        if (req.getWarehouseId() != null) {
            Warehouse wh = warehouseRepo.findById(req.getWarehouseId())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse not found: " + req.getWarehouseId()));
            rule.setWarehouse(wh);
        }

        WarehouseRule saved = ruleRepo.save(rule);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void deleteRule(UUID id) {
        if (!ruleRepo.existsById(id)) throw new EntityNotFoundException("Rule not found: " + id);
        ruleRepo.deleteById(id);
    }

    public WarehouseRuleResponseDto getById(UUID id) {
        WarehouseRule rule = ruleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found: " + id));
        return mapper.toResponse(rule);
    }

    public List<WarehouseRuleResponseDto> getAll() {
        List<WarehouseRule> rules = ruleRepo.findAll(); // hoặc findAllByEnabledTrueOrderByPriorityAsc tùy ý
        return mapper.toResponseList(rules);
    }
}