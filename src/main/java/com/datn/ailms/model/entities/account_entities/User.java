package com.datn.ailms.model.entities.account_entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
    @Column(length = 255)
    String name;

    String password;

    String phone;

    String email;

    boolean gender;

    LocalDate dob;

    @Column(length = 255)
    String address;

    @ManyToMany
//    @JoinTable(
//            name = "Users_roles",
//            joinColumns = @JoinColumn(name = "User_id"),
//            inverseJoinColumns = @JoinColumn(name = "roles_name")
//    )
    @JoinTable(
            name = "Users_roles",
            joinColumns = @JoinColumn(name = "User_id", referencedColumnName = "id"), // User PK
            inverseJoinColumns = @JoinColumn(name = "roles_name", referencedColumnName = "name") // Role PK
    )
    Set<Role> roles;

    boolean status;
}
