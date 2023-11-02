package org.weebook.api.dto;

import java.io.Serializable;

/**
 * DTO for {@link dev.weebook.api.entity.User}
 */
public record UserDto(
        String usernameUser,
        String emailUser,
        Boolean gender, String firstName, String lastName) implements Serializable {
}