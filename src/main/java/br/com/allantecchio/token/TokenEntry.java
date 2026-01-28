package br.com.allantecchio.token;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class TokenEntry {

    private String token;
    private LocalDateTime expireIn;
    private Duration ttl;

    public TokenEntry(Duration ttl) {
        this.ttl = ttl;
    }

    public boolean expired() {
        return expireIn == null || LocalDateTime.now().isAfter(expireIn);
    }

    public void renew(String newToken) {
        this.token = newToken;
        this.expireIn = LocalDateTime.now().plus(ttl);
    }

}
