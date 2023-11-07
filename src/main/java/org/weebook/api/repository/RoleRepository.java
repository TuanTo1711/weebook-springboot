package org.weebook.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.weebook.api.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}