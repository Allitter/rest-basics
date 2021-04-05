package com.epam.esm.repository.specification;

import com.epam.esm.model.Entity;

/**
 * The Specification interface.
 *
 * @param <T> the type of queried entities
 */
public interface Specification<T extends Entity> {
    /**
     * Query.
     *
     * @return the query string of specification
     */
    String query();

    /**
     * Get Condition.
     *
     * @return condition from where clause or empty string if there is no condition
     */
    String getCondition();

    /**
     * Get args.
     *
     * @return the array of specification parameters to prepare statement
     */
    Object[] getArgs();
}
