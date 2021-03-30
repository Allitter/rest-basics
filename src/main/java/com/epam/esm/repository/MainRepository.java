package com.epam.esm.repository;

import com.epam.esm.model.Entity;
import com.epam.esm.repository.specification.Specification;

import java.util.List;
import java.util.Optional;

public interface MainRepository<T extends Entity> {

    T add(T t);

    T update(T t);

    boolean remove(int id);

    List<T> query(Specification<T> specification);

    Optional<T> queryFirst(Specification<T> specification);
}
