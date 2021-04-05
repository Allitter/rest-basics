package com.epam.esm.service;

import com.epam.esm.model.Tag;

import java.util.List;

/**
 * The Tag service interface.
 */
public interface TagService {

    /**
     * Find by id tag dto.
     *
     * @param id the id to search
     * @return {@link Tag}
     */
    Tag findById(int id);

    /**
     * Finds all tags
     *
     * @return the list of all tags
     */
    List<Tag> findAll();

    /**
     * Adds tag to repository.
     *
     * @param tag the tag to be added
     * @return the added tag
     */
    Tag add(Tag tag);

    /**
     * Remove boolean.
     *
     * @param id the id
     * @return true if removed, false otherwise
     */
    boolean remove(int id);

}
