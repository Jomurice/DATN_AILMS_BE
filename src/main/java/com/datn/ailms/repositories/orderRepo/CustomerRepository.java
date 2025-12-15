package com.datn.ailms.repositories.orderRepo;

import com.datn.ailms.model.entities.order_entites.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query(value =
            "SELECT c FROM Customer c " +
                    "WHERE (:status IS NULL OR c.status = :status) " +
                    "AND ( :search IS NULL OR :search = '' OR " +
                    "   LOWER(FUNCTION('unaccent', CONCAT(c.lastName, ' ', c.firstName))) " +
                    "   LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :search, '%'))) " +
                    "   OR LOWER(FUNCTION('unaccent', c.firstName)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :search, '%'))) " +
                    "   OR LOWER(FUNCTION('unaccent', c.lastName)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :search, '%'))) " +
                    "   OR c.phone LIKE CONCAT('%', :search, '%')" +
                    ")"
    )
    Page<Customer> searchCustomer(@Param("search") String search,
                                  @Param("status") Boolean status,
                                  Pageable pageable);



    boolean existsByEmailAndIdNot(String email, UUID customerId);
    boolean existsByPhoneAndIdNot(String Phone, UUID customerId);
}
