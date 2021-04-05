package com.epam.esm.validation;

import com.epam.esm.dto.TagDto;

import java.util.Map;

public interface TagDtoValidator {
    Map<String, String> validate(TagDto dto);
}
