package com.epam.esm.validator;

import com.epam.esm.model.Tag;

import java.util.Map;

public interface TagValidator {
    Map<String, String> validate(Tag tag);
}
