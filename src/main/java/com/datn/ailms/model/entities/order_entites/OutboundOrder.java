package com.datn.ailms.model.entities.order_entites;

import com.datn.ailms.model.entities.account_entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class OutboundOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String code;
    String customer;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String status;

    @OneToMany(mappedBy = "outboundOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<OutboundOrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;
}
