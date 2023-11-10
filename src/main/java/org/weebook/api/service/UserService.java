package org.weebook.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserService extends UserDetailsManager {
    UserDetails loadUserByEmail(String email);
}
