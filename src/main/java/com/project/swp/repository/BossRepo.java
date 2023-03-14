package com.project.swp.repository;

import com.project.swp.entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BossRepo extends JpaRepository<Boss, String> {
    Optional<Boss> findFirstByUsernameAndPassword(String username, String password);
}
