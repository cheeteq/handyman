package com.jakubcitko.handyman.adapters.inbound.security;

import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoadUserAccountPort loadUserAccountPort;

    public UserDetailsServiceImpl(LoadUserAccountPort loadUserAccountPort) {
        this.loadUserAccountPort = loadUserAccountPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return loadUserAccountPort.loadUserByEmail(email)
                .map(account -> new User(
                        account.email(),
                        account.passwordHash(),
                        account.roles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.name()))
                                .collect(Collectors.toList())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}