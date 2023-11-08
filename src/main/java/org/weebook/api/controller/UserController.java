package org.weebook.api.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.weebook.api.dto.UserDto;
import org.weebook.api.dto.mapper.UserMapper;
import org.weebook.api.entity.User;

@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserDetailsManager userDetailsService;
    private final UserMapper userMapper;

    @GetMapping("get-by-username")
    public UserDto getUserByUsername(@RequestParam @NotNull @NotEmpty @NotBlank String username) {
        User userDetails = (User) userDetailsService.loadUserByUsername(username);
        return userMapper.toDto(userDetails);
    }
}
