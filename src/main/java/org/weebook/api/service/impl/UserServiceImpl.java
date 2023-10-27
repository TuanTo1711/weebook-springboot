package org.weebook.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.weebook.api.entity.User;
import org.weebook.api.repository.UserRepo;

import java.util.stream.Collectors;

@Service("UserDetailsService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Can not find by user name" +username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),

                user.getAuthorities()
                        .stream()
                        .collect(Collectors.toList())
        );
    }
}
