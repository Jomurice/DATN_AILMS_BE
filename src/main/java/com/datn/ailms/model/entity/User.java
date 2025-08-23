package com.datn.ailms.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(length = 255)
    String username;

    String name;

    String password;

    String phone;

    String email;

    boolean gender;

    @ManyToMany
    @JoinTable(
            name = "Users_roles",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_name")
    )
    Set<Role> roles;
}
