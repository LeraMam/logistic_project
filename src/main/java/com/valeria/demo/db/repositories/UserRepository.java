package com.valeria.demo.db.repositories;

import com.valeria.demo.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityById(Long id);
    Optional<UserEntity> findUserEntityByLogin(String login);
}
