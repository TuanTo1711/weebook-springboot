package org.weebook.api.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class SignInRequest {
    @NotNull(message = "Your username must be not null")
    @NotBlank(message = "Your username must be not blank")
    @NotEmpty(message = "Your username must be not empty")
    private String username;

    @NotNull(message = "Password must be not null")
    @Length(message = "Length must be between {min} and {max}", min = 6, max = 12)
    private String password;
}
