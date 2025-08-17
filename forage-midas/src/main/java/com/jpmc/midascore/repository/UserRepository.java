package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserRecord, Long> {
    Optional<UserRecord> findByName(String name);

    @Override
    Optional<UserRecord> findById(Long id);
}
