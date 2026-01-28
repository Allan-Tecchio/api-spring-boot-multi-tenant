package br.com.allantecchio.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoService {

    private static final String ALGO = "AES";
    private final SecretKeySpec key;

    public CryptoService(String secret) {
        byte[] k = secret.substring(0, 16).getBytes();
        this.key = new SecretKeySpec(k, ALGO);
    }

    public String encrypt(String value) {
        try {
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(c.doFinal(value.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
