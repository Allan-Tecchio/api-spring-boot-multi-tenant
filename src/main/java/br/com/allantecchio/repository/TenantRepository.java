package br.com.allantecchio.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TenantRepository {

    private final JdbcTemplate masterJdbcTemplate;

    public TenantDb findByTenantId(String tenantId) {
        return masterJdbcTemplate.queryForObject(
                """
                SELECT db_url, db_user, db_pass_enc
                FROM tenant_db
                WHERE tenant_id = ? AND ativo = true
                """,
                (rs, i) -> new TenantDb(
                        rs.getString("db_url"),
                        rs.getString("db_user"),
                        rs.getString("db_pass_enc")
                ),
                tenantId
        );
    }
}
