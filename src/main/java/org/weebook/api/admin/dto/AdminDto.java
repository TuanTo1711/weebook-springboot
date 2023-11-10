package org.weebook.api.admin.dto;

import org.weebook.api.dto.RoleDto;

import java.io.Serializable;

/**
 * DTO for {@link org.weebook.api.entity.User}
 */
public record AdminDto
        (String username,
         String email,
         String firstName,
         String lastName,
         Boolean gender,
         RoleDto role) implements Serializable {
}