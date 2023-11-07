package org.weebook.api.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInRequest {
    @NotNull(message = "username user is not Null")
    @NotBlank(message = "username user is not Blank")
    @NotEmpty(message = "username user is not Empty")
    private String username;

    private String password;
}
