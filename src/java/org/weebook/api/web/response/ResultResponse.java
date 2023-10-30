package org.weebook.api.web.response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ResultResponse<T>(
        Integer status,
        String message,
        T data

) implements Serializable {

}