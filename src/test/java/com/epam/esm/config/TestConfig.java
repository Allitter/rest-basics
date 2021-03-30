package com.epam.esm.config;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.mapper.CertificateMapper;
import com.epam.esm.repository.mapper.TagMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

    @Bean
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addDefaultScripts()
                .build();
    }

    @Bean
    public TagMapper tagMapper() {
        return new TagMapper();
    }

    @Bean
    public TagRepository tagRepository(TagMapper tagMapper) {
        return new TagRepository(jdbcTemplate(), tagMapper);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(h2DataSource());
    }

    @Bean
    public CertificateMapper certificateMapper() {
        return new CertificateMapper();
    }

    @Bean
    public CertificateRepository certificateRepository() {
        return new CertificateRepository(jdbcTemplate(), certificateMapper(), tagMapper());
    }
}
