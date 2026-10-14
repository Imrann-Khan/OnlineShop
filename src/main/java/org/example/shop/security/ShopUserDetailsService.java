package org.example.shop.security;

import org.example.shop.user.User;
import org.example.shop.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class ShopUserDetailsService implements UserDetailsService {

    private final UserService users;

    ShopUserDetailsService(UserService users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = users.findEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(String.valueOf(user.getId()))
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .build();
    }
}
