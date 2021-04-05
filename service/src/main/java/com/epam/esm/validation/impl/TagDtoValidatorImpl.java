package com.epam.esm.validation.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.util.ResourceBundleMessage;
import com.epam.esm.validation.TagDtoValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TagDtoValidatorImpl implements TagDtoValidator {
    private static final String NAME_ATTRIBUTE = "tag_name";

    @Override public Map<String, String> validate(TagDto dto) {
        Map<String, String> result = new HashMap<>();

        String name = dto.getName();
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
