package com.project.swp.service;

import com.project.swp.entity.Boss;
import com.project.swp.repository.BossRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BossService {
    @Autowired
    private BossRepo bossRepo;

    public Boss Authentication(String username, String password) {
        Boss boss = bossRepo.findFirstByUsernameAndPassword(username, password).orElse(null);
        return boss == null ? null : boss;
    }
}
