package com.finco.finco.infrastructure.config.authco.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.finco.finco.infrastructure.config.authco.dto.UserInfoDto;

@Service
public class AuthcoRestGateway {

    private final RestClient restClient;

    public AuthcoRestGateway(RestClient.Builder builder,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUrl) {
        this.restClient = builder.baseUrl(issuerUrl).build();
    }

    public UserInfoDto getUserInfo(String Token) {
        return restClient.get()
                .uri("/userinfo")
                .headers(h -> h.setBearerAuth(Token))
                .retrieve().body(UserInfoDto.class);
    }

}
