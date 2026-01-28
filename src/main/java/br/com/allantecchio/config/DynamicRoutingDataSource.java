package br.com.allantecchio.config;

import br.com.allantecchio.tenant.TenantContext;
import br.com.allantecchio.tenant.TenantDataSourceCache;
import br.com.allantecchio.tenant.TenantDb;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Collections;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final TenantDataSourceCache cache;

    public DynamicRoutingDataSource(TenantDataSourceCache cache) {
        this.cache = cache;

        // Default fake só para satisfazer o AbstractRoutingDataSource
        DriverManagerDataSource fake = new DriverManagerDataSource();
        fake.setDriverClassName("org.postgresql.Driver");
        fake.setUrl("jdbc:postgresql://localhost:5432/DO_NOT_USE");
        fake.setUsername("invalid");
        fake.setPassword("invalid");

        super.setTargetDataSources(Collections.emptyMap());
        super.setDefaultTargetDataSource(fake);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        TenantDb db = TenantContext.get();

        if (db == null) {
            throw new IllegalStateException("TenantContext não definido para a request");
        }

        return cache.getOrCreate(db);
    }
}
