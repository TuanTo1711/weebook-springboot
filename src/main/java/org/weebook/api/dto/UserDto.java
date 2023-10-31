package org.weebook.api.dto;

import java.io.Serializable;

/**
 * DTO for {@link dev.weebook.api.entity.User}
 */
public record UserDto(
        String usernameUser,
        String fullNameUser,
        String emailUser,
        Boolean gender) implements Serializable {
}