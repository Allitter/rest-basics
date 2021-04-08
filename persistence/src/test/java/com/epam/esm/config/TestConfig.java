package com.epam.esm.config;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.mapper.CertificateMapper;
import com.epam.esm.repository.mapper.TagMapper;
import com.epam.esm.repository.specification.SpecificationCompressor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
public class TestConfig {

    @Bean
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test-schema.sql")
                .addScript("classpath:test-data.sql")
                .build();
    }

    @Bean
    public TagMapper tagMapper() {
        return new TagMapper();
    }

    @Bean
    public TagRepository tagRepository(SpecificationCompressor<Tag> specificationCompressor) {
        return new TagRepository(jdbcTemplate(), tagMapper(), specificationCompressor);
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
    public CertificateRepository certificateRepository(SpecificationCompressor<Certificate> specificationCompressor) {
        return new CertificateRepository(jdbcTemplate(), certificateMapper(), tagMapper(), specificationCompressor);
    }
}
