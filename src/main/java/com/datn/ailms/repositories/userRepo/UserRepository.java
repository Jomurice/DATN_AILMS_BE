package com.datn.ailms.repositories.userRepo;

import com.datn.ailms.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findUserWithRoles(@Param("id") String id);


//    @Query(value = "SELECT * FROM users u JOIN user_roles ur ON u.id = ur.user_id WHERE u.id = :id", nativeQuery = true)
//    Optional<User> findUserWithRoles(@Param("id") String id);

}
