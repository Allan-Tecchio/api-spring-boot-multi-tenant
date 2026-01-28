package br.com.allantecchio.dto;

public record TenantCreateDTO(
        String tenantId,
        String dbUrl,
        String dbUser,
        String dbPass
) {

}