package com.finco.finco.infrastructure.config.security.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.infrastructure.config.db.mapper.UserMapper;
import com.finco.finco.infrastructure.config.db.repository.RoleRepository;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;
import com.finco.finco.infrastructure.config.security.dto.CustomOAuth2User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
       OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            Optional<UserSchema> userDb = userRepository.findByEmail(email);
            UserSchema user;

            if (userDb.isEmpty()) {
                user = new UserSchema();
                user.setEmail(email);
                user.setName(name);
                user.setEnable(true);
                user.setRegistrationDate(LocalDateTime.now());
                user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RoleNotFoundException())));

                user = userRepository.save(user);
            } else {
                user = userDb.get();
            }

            return new CustomOAuth2User(oAuth2User, userMapper.toLightUser(user));
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException("Error al procesar el login OAuth2: " + ex.getMessage());
        }


    }

    

}
