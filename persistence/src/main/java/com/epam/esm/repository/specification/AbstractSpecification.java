package com.epam.esm.repository.specification;

import com.epam.esm.model.Entity;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSpecification<T extends Entity> implements Specification<T> {
    private static final String WHERE_STATEMENT = "WHERE %s";
    private static final String QUERY = "SELECT * FROM %s %s";

    @Override
    public Object[] getArgs() {
        return new Object[0];
    }

    @Override
    public String query() {
        String whereStatement = !StringUtils.isBlank(getCondition())
                ? String.format(WHERE_STATEMENT, getCondition())
                : StringUtils.EMPTY;
        return String.format(QUERY, getTableName(), whereStatement);
    }

    protected abstract String getTableName();

}
