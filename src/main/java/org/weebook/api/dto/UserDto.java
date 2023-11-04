package org.weebook.api.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link org.weebook.api.entity.User}
 */
public record UserDto(
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean gender,
        LocalDate birthday,
        String avatarUrl) implements Serializable {
}