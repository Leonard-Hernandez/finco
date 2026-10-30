package com.finco.finco.infrastructure.config.authco.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.finco.finco.entity.security.exception.IdentityProviderUnavailableException;
import com.finco.finco.entity.security.exception.InvalidTokenException;
import com.finco.finco.infrastructure.config.authco.dto.UserInfoDto;

@Service
public class AuthcoRestGateway {

    private final RestClient restClient;

    public AuthcoRestGateway(RestClient.Builder builder,
            @Value("${finco.security.internal-url}") String authcoInternalUrl) {
        this.restClient = builder.baseUrl(authcoInternalUrl).build();
    }

    public UserInfoDto getUserInfo(String Token) {
        try {
            return restClient.get()
                    .uri("/userinfo")
                    .headers(h -> h.setBearerAuth(Token))
                    .retrieve()
                    .onStatus(status -> status.isSameCodeAs(HttpStatus.UNAUTHORIZED), (request, response) -> {
                        throw new InvalidTokenException();
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new IdentityProviderUnavailableException();
                    })
                    .body(UserInfoDto.class);
        } catch (ResourceAccessException e) {
            throw new IdentityProviderUnavailableException();
        }
    }

}
