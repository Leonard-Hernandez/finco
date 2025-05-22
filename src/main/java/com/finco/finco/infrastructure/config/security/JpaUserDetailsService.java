package com.finco.finco.infrastructure.config.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserSchema> userDb = userRepository.findByEmail(username);

        if (!userDb.isPresent()) {
            throw new UsernameNotFoundException("usename: "+ username);
        }

        UserSchema user = userDb.get();

        List<GrantedAuthority> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return new User(
            user.getEmail(),
            user.getPassword(),
            true,
            true,
            true,
            true,
            roles
            );
        
        }

}
