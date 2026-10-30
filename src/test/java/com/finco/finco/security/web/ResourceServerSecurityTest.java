package com.finco.finco.security.web;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.finco.finco.entity.pagination.PagedResult;
import com.finco.finco.entity.security.model.FincoScopes;
import com.finco.finco.infrastructure.config.security.SpringSecurityConfig;
import com.finco.finco.infrastructure.config.security.metadata.ProtectedResourceMetadataController;
import com.finco.finco.infrastructure.user.controller.GetAllUserController;
import com.finco.finco.usecase.user.GetAllUserUseCase;

@WebMvcTest(controllers = { ProtectedResourceMetadataController.class, GetAllUserController.class })
@Import(SpringSecurityConfig.class)
@TestPropertySource(properties = {
        "finco.frontend.url=http://localhost:4200",
        "finco.security.issuer=http://finco.localhost/authco",
        "finco.security.internal-url=http://authco:8087/authco",
        "finco.security.resource-id=http://finco.localhost/finco-api/v1",
        "finco.security.scopes-supported=openid,profile,email,finco:premium"
})
@DisplayName("Resource server security test")
public class ResourceServerSecurityTest {

    private static final String RESOURCE_METADATA = "resource_metadata=\"http://finco.localhost/finco-api/v1/.well-known/oauth-protected-resource\"";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetAllUserUseCase getAllUserUseCase;

    @Test
    @DisplayName("Protected resource metadata should be public")
    public void metadataShouldBePublic() throws Exception {
        mockMvc.perform(get("/.well-known/oauth-protected-resource"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resource").value("http://finco.localhost/finco-api/v1"))
                .andExpect(jsonPath("$.authorization_servers[0]").value("http://finco.localhost/authco"))
                .andExpect(jsonPath("$.scopes_supported", hasItem("finco:premium")))
                .andExpect(jsonPath("$.bearer_methods_supported[0]").value("header"));
    }

    @Test
    @DisplayName("Missing token should return 401 with resource_metadata")
    public void missingTokenShouldAdvertiseMetadata() throws Exception {
        mockMvc.perform(get("/sse"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE,
                        allOf(startsWith("Bearer"), containsString(RESOURCE_METADATA))));
    }

    @Test
    @DisplayName("Invalid token should return 401 invalid_token with resource_metadata")
    public void invalidTokenShouldAdvertiseMetadata() throws Exception {
        mockMvc.perform(get("/accounts").header(HttpHeaders.AUTHORIZATION, "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE,
                        allOf(containsString("error=\"invalid_token\""), containsString(RESOURCE_METADATA))));
    }

    @Test
    @DisplayName("MCP endpoint without premium scope should return 403 insufficient_scope")
    public void mcpWithoutPremiumShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/sse").with(jwt()))
                .andExpect(status().isForbidden())
                .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, containsString("insufficient_scope")));
    }

    @Test
    @DisplayName("Admin endpoint without finco:admin scope should return 403")
    public void adminWithoutFincoAdminShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/admin/users").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_admin"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin endpoint with finco:admin scope should return 200")
    public void adminWithFincoAdminShouldBeAllowed() throws Exception {
        when(getAllUserUseCase.execute(any(), any())).thenAnswer(invocation -> PagedResult.empty(invocation.getArgument(0)));

        mockMvc.perform(get("/admin/users").with(jwt().authorities(new SimpleGrantedAuthority(FincoScopes.SCOPE_ADMIN))))
                .andExpect(status().isOk());
    }

}
