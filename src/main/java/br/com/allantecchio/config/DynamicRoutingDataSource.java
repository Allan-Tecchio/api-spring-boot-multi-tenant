package br.com.allantecchio.config;

import br.com.allantecchio.tenant.TenantContext;
import br.com.allantecchio.tenant.TenantDataSourceCache;
import br.com.allantecchio.tenant.TenantDb;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final TenantDataSourceCache cache;

    public DynamicRoutingDataSource(TenantDataSourceCache cache) {
        this.cache = cache;

        // ⚠️ Datasource "fake" só pra satisfazer o Spring no boot
        DriverManagerDataSource dummy = new DriverManagerDataSource();
        dummy.setDriverClassName("org.postgresql.Driver");
        dummy.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dummy.setUsername("postgres");
        dummy.setPassword("postgres");

        Map<Object, Object> targets = new HashMap<>();
        targets.put("DUMMY", dummy);

        super.setTargetDataSources(targets);
        super.setDefaultTargetDataSource(dummy);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return "DYNAMIC";
    }

    @Override
    protected DataSource determineTargetDataSource() {
        TenantDb db = TenantContext.get();
        if (db == null) {
            return (DataSource) getResolvedDefaultDataSource();
        }
        return cache.getOrCreate(db);
    }
}
