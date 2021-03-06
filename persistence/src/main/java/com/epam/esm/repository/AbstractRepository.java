package com.epam.esm.repository;

import com.epam.esm.model.Entity;
import com.epam.esm.repository.specification.Specification;
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

    protected AbstractRepository(JdbcTemplate jdbcTemplate, RowMapper<T> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    protected int insert(String query, Object...params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 1; i <= params.length; i++) {
                ps.setObject(i, params[i - 1]);
            }
            return ps;
        };

        jdbcTemplate.update(psc, keyHolder);

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
    public boolean exists(Specification<?> specification) {
        String existsQuery = String.format(EXISTS_QUERY, specification.query());
        return jdbcTemplate.queryForObject(existsQuery, Boolean.class, specification.getArgs());
    }
}
