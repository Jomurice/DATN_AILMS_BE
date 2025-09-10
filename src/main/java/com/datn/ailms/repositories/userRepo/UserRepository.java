package com.datn.ailms.repositories.userRepo;

import com.datn.ailms.model.entities.account_entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    List<User> findByNameContainingIgnoreCase(String name);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findUserWithRoles(@Param("id") String id);


//    @Query(value = "SELECT * FROM users u JOIN user_roles ur ON u.id = ur.user_id WHERE u.id = :id", nativeQuery = true)
//    Optional<User> findUserWithRoles(@Param("id") String id);

}
