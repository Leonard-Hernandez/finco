package com.finco.finco.infrastructure.config.security.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.infrastructure.config.db.repository.RoleRepository;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        try {
            String email = extractEmail(oAuth2User, userRequest);
            String name = oAuth2User.getAttribute("name");
            attributes.put("email", email);

            Optional<UserSchema> userOptional = userRepository.findByEmail(email);
            UserSchema user = userOptional.orElseGet(() -> {
                UserSchema userNew = new UserSchema();
                userNew.setEmail(email);
                userNew.setPassword("Oauth");
                userNew.setName(name);
                userNew.setEnable(true);
                userNew.setRegistrationDate(LocalDateTime.now());
                userNew.setRoles(
                        List.of(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RoleNotFoundException())));

                return userRepository.save(userNew);
            });

            return new DefaultOAuth2User(
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .toList(),
                    attributes,
                    "email");

        } catch (Exception ex) {
            throw new OAuth2AuthenticationException("Error with oauth2: " + ex.getMessage());
        }

    }

    private String extractEmail(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            String accessToken = userRequest.getAccessToken().getTokenValue();
            email = getGithubUserEmail(accessToken);
        }
        return email;
    }

    private String getGithubUserEmail(String accessToken) {

        restTemplate = new RestTemplate();

        try {
            String emailsUrl = "https://api.github.com/user/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "token " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    emailsUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> emails = response.getBody();

            if (emails != null && !emails.isEmpty()) {
                for (Map<String, Object> emailEntry : emails) {
                    if (Boolean.TRUE.equals(emailEntry.get("primary"))
                            && Boolean.TRUE.equals(emailEntry.get("verified"))) {
                        return (String) emailEntry.get("email");
                    }
                }
            }

        } catch (RestClientException e) {
            System.err.println("Error with github: " + e.getMessage());
        }
        return null;
    }

}
