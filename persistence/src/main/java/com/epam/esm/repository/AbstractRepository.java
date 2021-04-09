package com.epam.esm.repository;

import com.epam.esm.exception.RepositoryError;
import com.epam.esm.model.Entity;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.impl.SpecificationCompressorImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<T extends Entity> implements MainRepository<T> {
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM %s WHERE id = ?";
    private static final String ID_ATTRIBUTE = "id";
    private static final String EXISTS_QUERY = "SELECT EXISTS(%s) as ex";
    private static final int ONE_ROW_AFFECTED = 1;
    private static final int FIRST_ELEMENT = 0;
    private static final int PREPARED_STATEMENT_PARAMETER_BIAS = 1;
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> mapper;
    protected final SpecificationCompressorImpl specificationCompressor;

    protected AbstractRepository(JdbcTemplate jdbcTemplate, RowMapper<T> mapper,
                                 SpecificationCompressorImpl specificationCompressor) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.specificationCompressor = specificationCompressor;
    }

    protected int insert(String query, Object... params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator statementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + PREPARED_STATEMENT_PARAMETER_BIAS, params[i]);
            }
            return ps;
        };

        jdbcTemplate.update(statementCreator, keyHolder);

        return (int) keyHolder.getKeys().get(ID_ATTRIBUTE);
    }

    @Override
    public boolean remove(long id) {
        String query = String.format(DELETE_BY_ID_QUERY, getTableName());
        return jdbcTemplate.update(query, id) == ONE_ROW_AFFECTED;
    }

    @Override
    public List<T> query(Specification<T> specification) {
        return jdbcTemplate.query(specification.query(), mapper, specification.getParameters());
    }

    @Override
    public List<T> query(List<Specification<T>> specifications) {
        if (CollectionUtils.isEmpty(specifications)) {
            throw new RepositoryError();
        }

        Specification<T> specification = specificationCompressor.buildFrom(specifications);
        return query(specification);
    }

    @Override
    public Optional<T> queryFirst(Specification<T> specification) {
        List<T> list = query(specification);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(FIRST_ELEMENT));
    }

    @Override
    public boolean exists(Specification<?> specification) {
        String existsQuery = String.format(EXISTS_QUERY, specification.query());
        return jdbcTemplate.queryForObject(existsQuery, Boolean.class, specification.getParameters());
    }

    protected abstract String getTableName();
}
