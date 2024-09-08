package com.keumbang.api.auth_server.user.repository;

import com.keumbang.api.auth_server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByAccount(String account);

    Optional<User> findByAccount(String username);
}
