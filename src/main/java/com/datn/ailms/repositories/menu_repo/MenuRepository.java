package com.datn.ailms.repositories.menu_repo;

import com.datn.ailms.model.entities.other_entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {
    boolean existsByTitle(String title);

    List<Menu> findByParentIsNull();
}
