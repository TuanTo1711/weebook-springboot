package org.weebook.api.admin.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
public class AdminManageRoleRequest {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,4})$";

    @NotNull(message = "Your username must be not null")
    @NotBlank(message = "Your username must be not blank")
    @NotEmpty(message = "Your username must be not empty")
    private String username;

    @NotNull(message = "Password must be not null")
    @Length(message = "Length must be between {min} and {max}", min = 6, max = 12)
    private String password;

    @NotNull(message = "Your first name user is not Null")
    @Length(message = "Length must be between {min} and {max}", min = 2, max = 30)
    private String firstName;

    @Length(message = "Length must be between {min} and {max}", min = 2, max = 30)
    private String lastName;

    @NotNull(message = "Your email must be not null")
    @Email(regexp = EMAIL_REGEX, message = "Your email address invalid")
    private String email;

    @NotNull(message = "Role must be not null")
    private String roleDto;
}
