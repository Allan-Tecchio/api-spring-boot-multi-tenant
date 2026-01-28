package br.com.allantecchio.config;

import br.com.allantecchio.tenant.TenantDataSourceCache;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // ===== MASTER (fixo) =====
    @Bean
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource masterDataSource() {
        return new HikariDataSource();
    }

    // ===== TENANT (dinâmico) =====
    @Bean
    @Primary
    public DataSource tenantDataSource(TenantDataSourceCache cache) {
        return new DynamicRoutingDataSource(cache);
    }

    // JdbcTemplate do MASTER
    @Bean
    public JdbcTemplate masterJdbcTemplate(
            @Qualifier("masterDataSource") DataSource ds
    ) {
        return new JdbcTemplate(ds);
    }
}
