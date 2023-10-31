package org.weebook.api.web.response;

import java.io.Serializable;

public record ResultResponse<T>(
        Integer status,
        String message,
        T data
) implements Serializable {

}