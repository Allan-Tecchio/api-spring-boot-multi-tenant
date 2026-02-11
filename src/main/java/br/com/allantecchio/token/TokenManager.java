package br.com.allantecchio.token;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenManager {

    private final Map<String, TokenEntry> tokens = new ConcurrentHashMap<>();

    public String getToken(String apiKey, Duration ttl) {

        TokenEntry entry = tokens.computeIfAbsent(apiKey, k -> new TokenEntry(ttl));

        synchronized (entry) {
            if (entry.expired()) {
                String newToken = callApi(apiKey);
                entry.renew(newToken);
            }
            return entry.getToken();
        }
    }

    // example String t1 = tokenManager.getToken("API_TI", Duration.ofHours(24));
    private String callApi(String apiKey) {
        // @Allan 27/01/2026 - call HTTP for renew token
        return "TOKEN_" + apiKey + "_" + System.currentTimeMillis();
    }

}
