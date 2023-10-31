package org.weebook.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.weebook.api.dto.RoleDto;
import org.weebook.api.dto.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private UserDto userDto;

    private String accessToken;

    @Builder.Default
    private String type = "Bearer ";

}