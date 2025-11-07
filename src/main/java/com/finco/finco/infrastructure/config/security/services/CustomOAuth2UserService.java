package com.finco.finco.infrastructure.config.security.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.finco.finco.entity.role.exception.RoleNotFoundException;
import com.finco.finco.infrastructure.config.db.repository.RoleRepository;
import com.finco.finco.infrastructure.config.db.repository.UserRepository;
import com.finco.finco.infrastructure.config.db.schema.UserSchema;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
       OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            if (email == null) {
                String accessToken = userRequest.getAccessToken().getTokenValue();
                email = getGithubUserEmail(accessToken);
                oAuth2User.getAttributes().put("email", email);
            }

            System.out.println("Email: " + email);
            System.out.println("Name: " + name);

            Optional<UserSchema> userDb = userRepository.findByEmail(email);
            UserSchema user;

            if (userDb.isEmpty()) {
                user = new UserSchema();
                user.setEmail(email);
                user.setPassword("Oauth");
                user.setName(name);
                user.setEnable(true);
                user.setRegistrationDate(LocalDateTime.now());
                user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RoleNotFoundException())));

                user = userRepository.save(user);
            } else {
                user = userDb.get();
            }

            return oAuth2User;
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException("Error al procesar el login OAuth2: " + ex.getMessage());
        }

    }

    private String getGithubUserEmail(String accessToken) {

        restTemplate = new RestTemplate();

        try {
            String emailsUrl = "https://api.github.com/user/emails";

            // 1. Configurar encabezados con el Access Token
            HttpHeaders headers = new HttpHeaders();
            // Usamos "token " para la autenticación de GitHub
            headers.set("Authorization", "token " + accessToken); 
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 2. Ejecutar la llamada a la API
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    emailsUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> emails = response.getBody();

            // 3. Buscar el email primario y verificado
            if (emails != null && !emails.isEmpty()) {
                for (Map<String, Object> emailEntry : emails) {
                    // Buscamos el email marcado como 'primary' y 'verified'
                    if (Boolean.TRUE.equals(emailEntry.get("primary")) && Boolean.TRUE.equals(emailEntry.get("verified"))) {
                        return (String) emailEntry.get("email");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al llamar a la API de emails de GitHub: " + e.getMessage());
        }
        return null;
    }

}
