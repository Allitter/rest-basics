package com.epam.esm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;

@Profile("prod")
@PropertySource("classpath:database.properties")
@Configuration
public class ProdConfig {
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String password;
    @Value("${pool.size}")
    private int poolSize;
    @Value("${driver}")
    private String driverClass;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource postgresDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClass);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);

        return new HikariDataSource(config);
    }
}
