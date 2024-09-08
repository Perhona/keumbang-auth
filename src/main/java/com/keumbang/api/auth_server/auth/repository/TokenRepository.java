package com.keumbang.api.auth_server.auth.repository;

import com.keumbang.api.auth_server.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUserId(Long userId);

    RefreshToken findByUserId(Long userId);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.userId = :userId AND rt.expiredAt > CURRENT_TIMESTAMP")
    Optional<RefreshToken> findValidRefreshTokensByUserId(@Param("userId") Long userId);
}
