package com.example.sennova.web.security;

import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.UserPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceSecurity implements UserDetailsService {

    @Autowired
    private final UserPersistencePort userPersistencePort;

    public UserServiceSecurity(@Lazy  UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserModel user = this.userPersistencePort.findByUsername(username);
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" +  user.getRole().getNameRole()))
                .build();
    }
}
