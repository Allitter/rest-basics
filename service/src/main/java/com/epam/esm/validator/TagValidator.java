package com.epam.esm.validator;

import com.epam.esm.model.Tag;

import java.util.Map;

/**
 * Tag validator.
 */
public interface TagValidator {
    /**
     * Validate.
     *
     * @param tag the tag
     * @return the map of validation fails (attribute; fail)
     */
    Map<String, String> validate(Tag tag);
}
