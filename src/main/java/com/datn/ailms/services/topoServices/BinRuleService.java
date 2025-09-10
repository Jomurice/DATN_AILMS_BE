package com.datn.ailms.services.topoServices;

import com.datn.ailms.interfaces.IBinRule;
import com.datn.ailms.model.dto.request.BinRuleRequestDto;
import com.datn.ailms.model.dto.response.BinRuleResponseDto;
import com.datn.ailms.model.entities.topo_entities.Bin;
import com.datn.ailms.model.entities.topo_entities.BinRule;
import com.datn.ailms.repositories.warehousetopology.BinRepository;
import com.datn.ailms.repositories.warehousetopology.BinRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BinRuleService implements IBinRule {
    private final BinRuleRepository _binRuleRepo;
    private final BinRepository _binRepo;

    /**
     * Tìm bin bằng quy tắc: load tất cả rule enabled theo priority, duyệt từ đầu -> first match wins.
     * Pattern được hiểu là Java regex; dùng Matcher.find() để cho phép regex không cần full-match.
     */
    @Override
    @Transactional()
    public Optional<Bin> findBinForSerialOptional(String serial) {
        if (serial == null) return Optional.empty();

        List<BinRule> rules = _binRuleRepo.findAllByEnabledTrueOrderByPriorityAsc();
        for (BinRule rule : rules) {
            String p = rule.getPattern();
            if (p == null || p.isBlank()) continue;
            try {
                Pattern pattern = Pattern.compile(p);
                if (pattern.matcher(serial).find()) {
                    // rule matches -> return bin (bin is eagerly fetched)
                    return Optional.ofNullable(rule.getBin());
                }
            } catch (Exception ex) {
                // Nếu regex sai thì bỏ qua rule (hoặc log)
                // logger.warn("Invalid pattern in BinRule {}: {}", rule.getId(), p);
            }
        }
        return Optional.empty();
    }

    @Override
    public Bin findBinForSerial(String serial) {
        return findBinForSerialOptional(serial)
                .orElseThrow(() -> new EntityNotFoundException("No bin rule matched for serial: " + serial));
    }

    // ---------------- CRUD rule ----------------

    @Override
    @Transactional
    public BinRuleResponseDto createRule(BinRuleRequestDto req) {
        UUID binId = req.getBinId();
        Bin bin = _binRepo.findById(binId)
                .orElseThrow(() -> new EntityNotFoundException("Bin not found: " + binId));

        BinRule r = BinRule.builder()
                .pattern(req.getPattern())
                .level(req.getLevel())
                .bin(bin)
                .priority(Optional.ofNullable(req.getPriority()).orElse(100))
                .enabled(Optional.ofNullable(req.getEnabled()).orElse(true))
                .build();

        BinRule saved = _binRuleRepo.save(r);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public BinRuleResponseDto updateRule(UUID id, BinRuleRequestDto req) {
        BinRule rule = _binRuleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BinRule not found: " + id));

        if (req.getPattern() != null) rule.setPattern(req.getPattern());
        if (req.getLevel() != null) rule.setLevel(req.getLevel());
        if (req.getPriority() != null) rule.setPriority(req.getPriority());
        if (req.getEnabled() != null) rule.setEnabled(req.getEnabled());
        if (req.getBinId() != null) {
            Bin bin = _binRepo.findById(req.getBinId())
                    .orElseThrow(() -> new EntityNotFoundException("Bin not found: " + req.getBinId()));
            rule.setBin(bin);
        }

        BinRule saved = _binRuleRepo.save(rule);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void deleteRule(UUID id) {
        if (!_binRuleRepo.existsById(id)) throw new EntityNotFoundException("BinRule not found: " + id);
        _binRuleRepo.deleteById(id);
    }

    @Override
    @Transactional()
    public List<BinRuleResponseDto> listRules() {
        List<BinRule> rules = _binRuleRepo.findAllByEnabledTrueOrderByPriorityAsc();
        List<BinRuleResponseDto> out = new ArrayList<>();
        for (BinRule r : rules) out.add(mapToResponse(r));
        return out;
    }

    private BinRuleResponseDto mapToResponse(BinRule r) {
        return BinRuleResponseDto.builder()
                .id(r.getId())
                .pattern(r.getPattern())
                .level(r.getLevel())
                .binId(r.getBin() != null ? r.getBin().getId() : null)
                .priority(r.getPriority())
                .enabled(r.getEnabled())
                .build();
    }
}
