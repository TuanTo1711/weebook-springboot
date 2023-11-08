package org.weebook.api.web.request;

import java.io.Serializable;

public record VerifyEmail(
        String email,
        String code
) implements Serializable {
}
