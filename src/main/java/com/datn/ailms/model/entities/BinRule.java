// package com.datn.ailms.model.entities;
package com.datn.ailms.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "bin_rules",
        indexes = {@Index(columnList = "priority")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Regex pattern (Java regex). Use "^A.*" for prefix 'A' etc.
     * You can also store simple prefix (e.g. "A") and interpret in service, but here we use regex.
     */
    @Column(nullable = false, length = 255)
    private String pattern;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RuleLevel level;

    /**
     * FK -> Bin (bin thật trong DB)
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", nullable = false)
    private Bin bin;

    /**
     * Ưu tiên: nhỏ hơn => ưu tiên hơn
     */
    @Column(nullable = false)
    private Integer priority = 100;

    /**
     * Nếu false thì rule tạm disabled và service sẽ bỏ qua
     */
    @Column(nullable = false)
    private Boolean enabled = true;
}
