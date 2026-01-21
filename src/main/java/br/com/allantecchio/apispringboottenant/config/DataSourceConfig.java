package br.com.allantecchio.apispringboottenant.config;

import br.com.allantecchio.apispringboottenant.tenant.TenantDataSourceCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(TenantDataSourceCache cache) {
        return new DynamicRoutingDataSource(cache);
    }
}
