package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.weebook.api.entity.User;
import org.weebook.api.repository.UserRepository;

import static org.weebook.api.exception.error.ErrorMessages.ACCOUNT_NOT_FOUND_ERROR;

@Service("userDetailsService")
@RequiredArgsConstructor
@Slf4j
@Setter
public class UserServiceImpl implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private SecurityContextHolderStrategy securityContextHolderStrategy
            = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "Username must be not null");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ACCOUNT_NOT_FOUND_ERROR));
    }

    @Override
    public void createUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Assert.isTrue(!this.userExists(username), "User with username: %s exists" .formatted(username));
        User user = (User) userDetails;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User user = (User) userDetails;
        Assert.isTrue(this.userExists(user.getUsername()), "User not exits");
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = currentUser.getName();
            User user = (User) loadUserByUsername(username);
            Assert.state(user != null, "Current user doesn't exist in database.");
            Assert.state(passwordEncoder.matches(oldPassword, user.getPassword()), "Old password don't match.");
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.saveAndFlush(user);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
