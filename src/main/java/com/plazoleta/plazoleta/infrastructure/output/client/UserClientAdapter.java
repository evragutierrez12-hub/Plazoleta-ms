package com.plazoleta.plazoleta.infrastructure.output.client;

import com.plazoleta.plazoleta.domain.exception.ExternalServiceException;
import com.plazoleta.plazoleta.domain.spi.UserClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

/**
 * Llama a usuarios-ms por HTTP, reenviando el mismo token JWT de la petición original.
 * La URL base sale de la propiedad usuarios.url.
 */
@Component
public class UserClientAdapter implements UserClientPort {

    private final RestClient client;

    public UserClientAdapter(@Value("${usuarios.url}") String usuariosUrl) {
        this.client = RestClient.builder().baseUrl(usuariosUrl).build();
    }

    @Override
    public Optional<String> findRole(Long userId) {
        try {
            RestClient.RequestHeadersSpec<?> request = client.get()
                    .uri("/api/v1/users/{id}/role", userId);
            String token = currentToken();
            if (token != null) {
                request = request.header("Authorization", "Bearer " + token);
            }
            String role = request.retrieve().body(String.class);
            return Optional.ofNullable(role).map(String::trim);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            throw new ExternalServiceException("usuarios-ms rechazó la consulta (" + e.getStatusCode().value() + ")");
        } catch (ResourceAccessException e) {
            throw new ExternalServiceException("No se pudo conectar con usuarios-ms. Verifica que esté corriendo.");
        }
    }

    private String currentToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getCredentials() instanceof String token) {
            return token;
        }
        return null;
    }
}
