package org.weebook.api.web.response;

import java.io.Serializable;

public record ErrorResponse<T>(
        Integer status,
        String message,
        T error
) implements Serializable {
}