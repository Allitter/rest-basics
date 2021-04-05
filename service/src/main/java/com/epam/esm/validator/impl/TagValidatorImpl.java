package com.epam.esm.validator.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.util.ResourceBundleMessage;
import com.epam.esm.validator.TagValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TagValidatorImpl implements TagValidator {
    private static final String NAME_ATTRIBUTE = "tag_name";

    @Override
    public Map<String, String> validate(Tag tag) {
        Map<String, String> result = new HashMap<>();

        String name = tag.getName();
        if (name == null) {
            result.put(NAME_ATTRIBUTE, ResourceBundleMessage.TAG_NAME_EMPTY);
            return result;
        }

        name = name.trim();
        if (name.length() < 2 || name.length() > 64) {
            result.put(NAME_ATTRIBUTE, ResourceBundleMessage.TAG_NAME_FORMAT);
        }

        return result;
    }

}
