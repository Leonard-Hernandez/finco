package com.finco.finco.infrastructure.config.security;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.finco.finco.entity.security.model.FincoScopes;
import com.finco.finco.infrastructure.config.security.entrypoint.ResourceMetadataAccessDeniedHandler;
import com.finco.finco.infrastructure.config.security.entrypoint.ResourceMetadataAuthenticationEntryPoint;
import com.finco.finco.infrastructure.config.security.jwt.JwtDecoderConfig;
import com.finco.finco.infrastructure.config.security.metadata.ProtectedResourceMetadataController;

@Configuration
@EnableWebSecurity
@EnableWebSocketSecurity
@Import(JwtDecoderConfig.class)
public class SpringSecurityConfig {

    private static final String MCP_SESSION_ID_HEADER = "Mcp-Session-Id";
    private static final String MCP_PROTOCOL_VERSION_HEADER = "mcp-protocol-version";

    @Value("${finco.frontend.url}")
    private String frontendUrl;

    @Value("${finco.security.resource-id}")
    private String resourceId;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Scope-protected paths, shared by the authorization rules and the 403 step-up challenge
        PathPatternRequestMatcher.Builder paths = PathPatternRequestMatcher.withDefaults();
        RequestMatcher adminPaths = paths.matcher("/admin/**");
        RequestMatcher mcpPaths = new OrRequestMatcher(paths.matcher("/sse/**"), paths.matcher("/mcp/message/**"));
        String resourceMetadataUrl = resourceId + ProtectedResourceMetadataController.PATH;

        return http.authorizeHttpRequests((authz) -> authz
                .requestMatchers(HttpMethod.GET, "/accounts/currencies").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET, ProtectedResourceMetadataController.PATH,
                        ProtectedResourceMetadataController.PATH + "/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**" ,"/ws/**").permitAll()
                .requestMatchers(adminPaths).hasAuthority(SecurityAuthorities.scope(FincoScopes.ADMIN))
                .requestMatchers(mcpPaths).hasAuthority(SecurityAuthorities.scope(FincoScopes.PREMIUM))
                .anyRequest().authenticated())
                .oauth2ResourceServer(rs -> rs
                        .jwt(Customizer.withDefaults())
                        // Also registered as the default entry point, so it covers missing and invalid tokens
                        .authenticationEntryPoint(new ResourceMetadataAuthenticationEntryPoint(resourceMetadataUrl))
                        .accessDeniedHandler(new ResourceMetadataAccessDeniedHandler(resourceMetadataUrl,
                                Map.of(adminPaths, FincoScopes.ADMIN, mcpPaths, FincoScopes.PREMIUM))))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(frontendUrl));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type",
                MCP_PROTOCOL_VERSION_HEADER, MCP_SESSION_ID_HEADER));
        corsConfiguration.setExposedHeaders(Arrays.asList(HttpHeaders.WWW_AUTHENTICATE, MCP_SESSION_ID_HEADER));
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;

    }

    @Bean
    AuthorizationManager<Message<?>> authorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages.simpTypeMatchers(SimpMessageType.DISCONNECT, SimpMessageType.HEARTBEAT,
                SimpMessageType.UNSUBSCRIBE).permitAll().anyMessage()
                .hasAuthority(SecurityAuthorities.scope(FincoScopes.PREMIUM));
        return messages.build();
    }

}
