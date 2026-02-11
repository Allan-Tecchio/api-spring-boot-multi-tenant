package br.com.allantecchio.api;

import br.com.allantecchio.dto.TenantCreateDTO;
import br.com.allantecchio.security.CryptoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Void> create(@RequestBody(required = false) TenantCreateDTO dto) {

        validInformations(dto);

        try {
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

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Connection already exist"
            );
        }
    }

    private void validInformations(TenantCreateDTO dto) {

        if (dto == null ||
                dto.dbUrl() == null ||
                dto.dbUser() == null ||
                dto.dbPass() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Some required connection fields are missing or null."
            );
        }
    }

    @PutMapping("/tenants/{tenantId}")
    public ResponseEntity<Void> update(
            @PathVariable String tenantId,
            @RequestBody TenantCreateDTO dto
    ) {
        validInformations(dto);

        String enc = crypto.encrypt(dto.dbPass());

        int rows = masterJdbcTemplate.update(
                """
                UPDATE tenant_db
                   SET db_url = ?,
                       db_user = ?,
                       db_pass_enc = ?
                 WHERE tenant_id = ?
                """,
                dto.dbUrl(),
                dto.dbUser(),
                enc,
                tenantId
        );

        if (rows == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Tenant not found"
            );
        }

        return ResponseEntity.noContent().build();
    }


}
