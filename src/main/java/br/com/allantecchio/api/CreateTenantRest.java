package br.com.allantecchio.api;

import br.com.allantecchio.dto.TenantCreateDTO;
import br.com.allantecchio.security.CryptoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class CreateTenantRest {

    private final JdbcTemplate masterJdbcTemplate;
    private final CryptoService crypto;

    public CreateTenantRest(
            @Qualifier("masterJdbcTemplate") JdbcTemplate masterJdbcTemplate,
            CryptoService crypto
    ) {
        this.masterJdbcTemplate = masterJdbcTemplate;
        this.crypto = crypto;
    }

    @PostMapping("/create-tenant")
    public void create(@RequestBody TenantCreateDTO dto) {

        String enc = crypto.encrypt(dto.dbPass());

        masterJdbcTemplate.update(
                """
                INSERT INTO tenant_db(tenant_id, db_url, db_user, db_pass_enc)
                VALUES (?,?,?,?)
                """,
                dto.tenantId(),
                dto.dbUrl(),
                dto.dbUser(),
                enc
        );
    }
}
