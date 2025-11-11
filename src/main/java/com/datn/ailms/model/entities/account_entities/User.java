package com.datn.ailms.model.entities.account_entities;

import com.datn.ailms.model.entities.order_entites.OutboundOrder;
import com.datn.ailms.model.entities.order_entites.PurchaseOrder;
import com.datn.ailms.model.entities.order_entites.PurchaseOrderItem;
import com.datn.ailms.model.entities.product_entities.ProductDetail;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

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
    UUID id;

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


    @OneToMany(mappedBy = "createdBy")
    private Set<PurchaseOrder> createdOrders;

    @OneToMany(mappedBy = "createdBy")
    private Set<OutboundOrder> createOutbound;

    @OneToMany(mappedBy = "exportedBy")
    private Set<OutboundOrder> exportedOutbound;

    @OneToMany(mappedBy = "scannedBy")
    private Set<ProductDetail> scannedItems;
}
