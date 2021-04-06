package com.epam.esm.repository;

import com.epam.esm.exception.RepositoryError;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Entity;
import com.epam.esm.repository.specification.CertificateSpecificationCompressor;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.SpecificationCompressor;
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
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> mapper;
    protected final SpecificationCompressor<T> specificationCompressor;

    protected AbstractRepository(JdbcTemplate jdbcTemplate, RowMapper<T> mapper,
                                 SpecificationCompressor<T> specificationCompressor) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.specificationCompressor = specificationCompressor;
    }

    protected int insert(String query, Object...params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator statementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 1; i <= params.length; i++) {
                ps.setObject(i, params[i - 1]);
            }
            return ps;
        };

        jdbcTemplate.update(statementCreator, keyHolder);

        return (int) keyHolder.getKeys().get(ID_ATTRIBUTE);
    }

    @Override
    public boolean remove(int id) {
        String query = String.format(DELETE_BY_ID_QUERY, getTableName());
        return jdbcTemplate.update(query, id) == 1;
    }

    protected abstract String getTableName();

    @Override
    public List<T> query(Specification<T> specification) {
        return jdbcTemplate.query(specification.query(), mapper, specification.getArgs());
    }

    @Override
    public Optional<T> queryFirst(Specification<T> specification) {
        List<T> list = query(specification);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<T> query(List<Specification<T>> specifications)  {
        if (specifications == null || specifications.isEmpty()) {
            throw new RepositoryError();
        }

        Specification<T> specification = specificationCompressor.buildFrom(specifications);
        return query(specification);
    }

    @Override
    public boolean exists(Specification<?> specification) {
        String existsQuery = String.format(EXISTS_QUERY, specification.query());
        return jdbcTemplate.queryForObject(existsQuery, Boolean.class, specification.getArgs());
    }
}
