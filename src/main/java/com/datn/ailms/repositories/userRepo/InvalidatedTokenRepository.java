package com.datn.ailms.repositories.userRepo;

import com.datn.ailms.model.entities.account_entities.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
