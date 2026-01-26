package com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa.repository;

import com.lilamaris.stockwolf.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
