package com.keumbang.api.auth_server.auth.repository;

import com.keumbang.api.auth_server.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUserId(Long userId);

    RefreshToken findByUserId(Long userId);
}
