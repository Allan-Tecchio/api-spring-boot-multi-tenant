package br.com.allantecchio.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class MasterSchemaInit {

    @Bean
    public boolean initMasterSchema(
            @Qualifier("masterDataSource") DataSource master
    ) {
        ResourceDatabasePopulator populator =
                new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));

        populator.execute(master);
        return true;
    }
}
