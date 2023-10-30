package org.weebook.api.dto;


import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link org.weebook.api.entity.Role}
 */
public record RoleDto(String name, Set<String> permissions) implements Serializable {

}