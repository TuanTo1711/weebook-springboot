package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.weebook.api.entity.User;

import java.util.Optional;
@EnableJpaRepositories

public interface UserRepo extends JpaSpecificationExecutor<User>, JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    User findByEmail(String emailUser);

    User findUserByUsername(String username);

    void deleteByUsername(String username);
}
