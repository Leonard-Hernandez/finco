package com.finco.finco.security.gateway;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.finco.finco.entity.security.exception.IdentityProviderUnavailableException;
import com.finco.finco.entity.security.exception.InvalidTokenException;
import com.finco.finco.infrastructure.config.authco.dto.UserInfoDto;
import com.finco.finco.infrastructure.config.authco.gateway.AuthcoRestGateway;

@DisplayName("Authco rest gateway test")
public class AuthcoRestGatewayTest {

    private static final String AUTHCO_INTERNAL_URL = "http://authco:8087/authco";
    private static final String TOKEN = "token";

    private MockRestServiceServer server;
    private AuthcoRestGateway authcoRestGateway;

    @BeforeEach
    public void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        authcoRestGateway = new AuthcoRestGateway(builder, AUTHCO_INTERNAL_URL);
    }

    @Test
    @DisplayName("Get user info successfully")
    public void getUserInfoSuccess() {
        // Arrange
        server.expect(requestTo(AUTHCO_INTERNAL_URL + "/userinfo"))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN))
                .andRespond(withSuccess(
                        "{\"sub\":\"sub-1\",\"email\":\"test@example.com\",\"email_verified\":true,\"name\":\"Test\"}",
                        MediaType.APPLICATION_JSON));

        // Act
        UserInfoDto result = authcoRestGateway.getUserInfo(TOKEN);

        // Assert
        assertEquals("test@example.com", result.email());
        assertTrue(result.emailVerified());
        server.verify();
    }

    @Test
    @DisplayName("Authco 401 should throw InvalidTokenException")
    public void unauthorizedShouldThrowInvalidToken() {
        // Arrange
        server.expect(requestTo(AUTHCO_INTERNAL_URL + "/userinfo")).andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> authcoRestGateway.getUserInfo(TOKEN));
        server.verify();
    }

    @Test
    @DisplayName("Authco 5xx should throw IdentityProviderUnavailableException")
    public void serverErrorShouldThrowUnavailable() {
        // Arrange
        server.expect(requestTo(AUTHCO_INTERNAL_URL + "/userinfo")).andRespond(withStatus(HttpStatus.BAD_GATEWAY));

        // Act & Assert
        assertThrows(IdentityProviderUnavailableException.class, () -> authcoRestGateway.getUserInfo(TOKEN));
        server.verify();
    }

    @Test
    @DisplayName("Authco unreachable should throw IdentityProviderUnavailableException")
    public void unreachableShouldThrowUnavailable() {
        // Arrange
        server.expect(requestTo(AUTHCO_INTERNAL_URL + "/userinfo")).andRespond(withException(new IOException("Connection refused")));

        // Act & Assert
        assertThrows(IdentityProviderUnavailableException.class, () -> authcoRestGateway.getUserInfo(TOKEN));
        server.verify();
    }

}
