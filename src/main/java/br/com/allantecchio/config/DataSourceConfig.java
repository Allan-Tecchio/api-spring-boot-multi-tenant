package br.com.allantecchio.config;

import br.com.allantecchio.tenant.TenantDataSourceCache;
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
