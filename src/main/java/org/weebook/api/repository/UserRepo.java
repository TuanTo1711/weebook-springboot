package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.weebook.api.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaSpecificationExecutor<User>, JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    Optional<Object> findByEmail(String emailUser);
}
