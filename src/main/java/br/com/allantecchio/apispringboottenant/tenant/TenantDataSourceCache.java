package br.com.allantecchio.apispringboottenant.tenant;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantDataSourceCache {

    private final ConcurrentHashMap<String, DataSource> cache = new ConcurrentHashMap<>();

    public DataSource getOrCreate(TenantDb db) {
        String key = db.getUrl() + "|" + db.getUser();

        return cache.computeIfAbsent(key, k -> {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl(db.getUrl());
            ds.setUsername(db.getUser());
            ds.setPassword(db.getPass());
            return ds;
        });
    }
}
