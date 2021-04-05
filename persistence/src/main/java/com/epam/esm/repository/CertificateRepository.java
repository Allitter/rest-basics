package com.epam.esm.repository;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.specification.CertificateByIdSpecification;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.TagByCertificateIdSpecification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CertificateRepository extends AbstractRepository<Certificate> {
    private static final String TABLE_NAME = "certificate";
    private static final String INSERT_QUERY = "INSERT INTO certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE certificate SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?;";
    private static final String ADD_TAG_QUERY = "INSERT INTO certificate_tag(id_certificate, id_tag) VALUES (?, ?);";
    private static final String REMOVE_CERTIFICATE_TAGS_QUERY = "DELETE FROM certificate_tag WHERE id_certificate = ?;";


    private final RowMapper<Tag> tagRowMapper;

    public CertificateRepository(JdbcTemplate jdbcTemplate, RowMapper<Certificate> mapper, RowMapper<Tag> tagRowMapper) {
        super(jdbcTemplate, mapper);
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Certificate add(Certificate certificate) {
        List<Object> fields = extractNonIdFields(certificate);
        int certificateId = insert(INSERT_QUERY, fields.toArray());
        List<Tag> tags = certificate.getTags();
        tags.forEach(tag -> jdbcTemplate.update(ADD_TAG_QUERY, certificateId, tag.getId()));

        return this.queryFirst(new CertificateByIdSpecification(certificateId)).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Certificate update(Certificate certificate) {
        List<Object> fields = extractNonIdFields(certificate);
        int certificateId = certificate.getId();
        fields.add(certificateId);
        jdbcTemplate.update(UPDATE_QUERY, fields.toArray());

        jdbcTemplate.update(REMOVE_CERTIFICATE_TAGS_QUERY, certificateId);
        List<Tag> tags = certificate.getTags();
        tags.forEach(tag -> jdbcTemplate.update(ADD_TAG_QUERY, certificateId, tag.getId()));

        return this.queryFirst(new CertificateByIdSpecification(certificateId)).orElseThrow(EntityNotFoundException::new);
    }

    private List<Object> extractNonIdFields(Certificate certificate) {
        List<Object> fields = Arrays.asList(
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate()
        );

        return new ArrayList<>(fields);
    }

    @Override
    public List<Certificate> query(Specification<Certificate> specification) {
        List<Certificate> certificates = super.query(specification);

        List<Certificate> taggedCertificates = new ArrayList<>();

        certificates.forEach(certificate -> {
            List<Tag> tags = getCertificateTags(certificate.getId());
            Certificate taggedCertificate = new Certificate.Builder(certificate).setTags(tags).build();
            taggedCertificates.add(taggedCertificate);
        });

        return taggedCertificates;
    }

    private List<Tag> getCertificateTags(int id) {
        Specification<Tag> tagSpecification = new TagByCertificateIdSpecification(id);
        return jdbcTemplate.query(tagSpecification.query(), tagRowMapper, tagSpecification.getArgs());
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }
}
