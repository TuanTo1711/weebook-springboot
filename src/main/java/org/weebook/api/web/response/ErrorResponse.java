package org.weebook.api.web.response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ErrorResponse<T>(
        Integer status,
        String message,
        T error
) implements Serializable {
}