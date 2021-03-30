package com.epam.esm.repository;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.specification.TagByIdSpecification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepository extends AbstractRepository<Tag> {
    private static final String TABLE_NAME = "tag";
    private static final String INSERT_QUERY = "INSERT INTO tag (name) VALUES (?);";
    private static final String UPDATE_QUERY = "UPDATE tag SET name = ? WHERE id = ?;";

    public TagRepository(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Tag add(Tag tag) {
        String name = tag.getName();
        int id = insert(INSERT_QUERY, name);
        return queryFirst(new TagByIdSpecification(id)).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Tag update(Tag tag) {
        int id = tag.getId();
        String name = tag.getName();
        jdbcTemplate.update(UPDATE_QUERY, name, id);
        return queryFirst(new TagByIdSpecification(id)).orElseThrow(EntityNotFoundException::new);
    }
}
