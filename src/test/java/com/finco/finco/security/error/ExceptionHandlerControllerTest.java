package com.finco.finco.security.error;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finco.finco.entity.security.exception.IdentityProviderUnavailableException;
import com.finco.finco.entity.security.exception.InvalidTokenException;
import com.finco.finco.infrastructure.config.error.ExceptionHandlerController;

@DisplayName("Identity provider error mapping test")
public class ExceptionHandlerControllerTest {

    private MockMvc mockMvc;

    @RestController
    static class ThrowingController {

        @GetMapping("/invalid-token")
        public void invalidToken() {
            throw new InvalidTokenException();
        }

        @GetMapping("/provider-unavailable")
        public void providerUnavailable() {
            throw new IdentityProviderUnavailableException();
        }

    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    @DisplayName("InvalidTokenException should return 401")
    public void invalidTokenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("InvalidTokenException"));
    }

    @Test
    @DisplayName("IdentityProviderUnavailableException should return 503")
    public void providerUnavailableShouldReturnServiceUnavailable() throws Exception {
        mockMvc.perform(get("/provider-unavailable"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("IdentityProviderUnavailableException"));
    }

}
