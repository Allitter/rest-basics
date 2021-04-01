package com.epam.esm.service;

import com.epam.esm.controller.dto.TagDto;

import java.util.List;

/**
 * The Tag service interface.
 */
public interface TagService {

    /**
     * Find by id tag dto.
     *
     * @param id the id to search
     * @return {@link TagDto}
     */
    TagDto findById(int id);

    /**
     * Finds all tags
     *
     * @return the list of all tags
     */
    List<TagDto> findAll();

    /**
     * Adds tag to repository.
     *
     * @param dto the tag to be added
     * @return the added tag
     */
    TagDto add(TagDto dto);

    /**
     * Remove boolean.
     *
     * @param id the id
     * @return true if removed, false otherwise
     */
    boolean remove(int id);

}
