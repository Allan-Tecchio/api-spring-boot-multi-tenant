package br.com.allantecchio.config;

import br.com.allantecchio.security.CryptoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public CryptoService cryptoService(
            @Value("${security.crypto-secret}") String secret
    ) {
        return new CryptoService(secret);
    }
}
