package com.epam.esm.repository;

import com.epam.esm.model.Entity;
import com.epam.esm.repository.specification.Specification;

import java.util.List;
import java.util.Optional;

/**
 * The interface Main repository.
 *
 * @param <T> the type of stored entities
 */
public interface MainRepository<T extends Entity> {

    /**
     * Add.
     *
     * @param t the entity to be added
     * @return the added entity
     */
    T add(T t);

    /**
     * Update.
     *
     * @param t the entity to be updated
     * @return the updated entity
     */
    T update(T t);

    /**
     * Remove.
     *
     * @param id the id of entity to be removed
     * @return true if removed, false otherwise
     */
    boolean remove(int id);

    /**
     * Query.
     *
     * @param specification the specification of queried entities {@link Specification}
     * @return the list of entities matching the specification
     */
    List<T> query(Specification<T> specification);

    /**
     * Query first. The same as query() method but returns only the first entity if such exists
     *
     * @param specification the specification of queried entities {@link Specification}
     * @return first of entities matching the specification or empty optional
     */
    Optional<T> queryFirst(Specification<T> specification);

    boolean exists(Specification<?> specification);
}
