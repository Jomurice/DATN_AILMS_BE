package com.datn.ailms.model.entities.account_entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="roles")
@Builder
public class Role {
    @Column(length = 255)
    @Id
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;
}
