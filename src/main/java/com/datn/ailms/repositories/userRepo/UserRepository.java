package com.datn.ailms.repositories.userRepo;

import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.services.stats.UserStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles")
    List<User> findAllWithRoles();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findUserWithRoles(@Param("id") String id);


    @Query(value = """
        SELECT DISTINCT u.* 
        FROM users u
        LEFT JOIN users_roles r ON u.id = r.user_id
        WHERE (:name IS NULL OR unaccent(lower(u.name)) LIKE unaccent(lower(CONCAT('%', :name, '%'))))
          AND (:role IS NULL OR r.roles_name = :role)
          AND (:status IS NULL OR u.status = :status)
          AND (:gender IS NULL OR u.gender = :gender)
        """,
            countQuery = """
            SELECT COUNT(DISTINCT u.id) 
            FROM users u
            LEFT JOIN users_roles r ON u.id = r.user_id
            WHERE (:name IS NULL OR unaccent(lower(u.name)) LIKE unaccent(lower(CONCAT('%', :name, '%'))))
              AND (:role IS NULL OR r.roles_name = :role)
              AND (:status IS NULL OR u.status = :status)
              AND (:gender IS NULL OR u.gender = :gender)
        """,
            nativeQuery = true)
    Page<User> searchUsers(
            @Param("name") String name,
            @Param("role") String role,
            @Param("status") Boolean status,
            @Param("gender") Boolean gender,
            Pageable pageable);
    @Query(value = """
        SELECT 
            COUNT(*) AS totalUsers, 
            COUNT(CASE WHEN gender = true THEN 1 END) AS totalMales,
            COUNT(CASE WHEN gender = false THEN 1 END) AS totalFemales,
            COUNT(CASE WHEN status = true THEN 1 END) AS totalActives,
            COUNT(CASE WHEN status = false THEN 1 END) AS totalBlocked
        FROM users
        """, nativeQuery = true)
    UserStatsDto countUserStats();
}
