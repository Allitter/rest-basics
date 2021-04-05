package com.epam.esm.validation.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.util.ResourceBundleMessage;
import com.epam.esm.validation.CertificateDtoValidator;
import com.epam.esm.validation.TagDtoValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateDtoValidatorImpl implements CertificateDtoValidator {
    private static final String DURATION_ATTRIBUTE = "duration";
    private static final String PRICE_ATTRIBUTE = "price";
    private static final String DESCRIPTION_ATTRIBUTE = "description";
    private static final String NAME_ATTRIBUTE = "certificate_name";
    private final TagDtoValidator tagDtoValidator;

    public CertificateDtoValidatorImpl(TagDtoValidator tagDtoValidator) {
        this.tagDtoValidator = tagDtoValidator;
    }

    @Override public Map<String, String> validateForCreate(CertificateDto dto) {
        Map<String, String> result = new HashMap<>();
        result.putAll(validateName(dto.getName()));
        result.putAll(validateDescription(dto.getDescription()));
        result.putAll(validatePrice(dto.getPrice()));
        result.putAll(validateDuration(dto.getDuration()));
        result.putAll(validateTags(dto.getTags()));

        return result;
    }

    @Override public Map<String, String> validateForUpdate(CertificateDto dto) {
        Map<String, String> result = new HashMap<>();
        if (!isEmpty(dto.getName())) {
            result.putAll(validateName(dto.getName()));
        }
        if (!isEmpty(dto.getDescription())) {
            result.putAll(validateDescription(dto.getDescription()));
        }
        if (dto.getDuration() != 0) {
            result.putAll(validateDuration(dto.getDuration()));
        }
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            result.putAll(validateTags(dto.getTags()));
        }
        result.putAll(validatePrice(dto.getPrice()));

        return result;
    }

    public Map<String, String> validateName(String name) {
        Map<String, String> result = new HashMap<>();

        if (isEmpty(name)) {
            result.put(NAME_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_NAME_EMPTY);
            return result;
        }
        name = name.trim();
        if (name.length() < 2 || name.length() > 64) {
            result.put(NAME_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_NAME_FORMAT);
        }

        return result;
    }

    public Map<String, String> validateDescription(String description) {
        Map<String, String> result = new HashMap<>();

        if (isEmpty(description)) {
            result.put(DESCRIPTION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DESCRIPTION_EMPTY);
            return result;
        }
        description = description.trim();
        if (description.length() < 2 || description.length() > 255) {
            result.put(DESCRIPTION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DESCRIPTION_FORMAT);
        }

        return result;
    }

    public Map<String, String> validatePrice(int price) {
        Map<String, String> result = new HashMap<>();
        if (price < 0) {
            result.put(PRICE_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_PRICE_FORMAT);
        }

        return result;
    }

    public Map<String, String> validateDuration(int duration) {
        Map<String, String> result = new HashMap<>();
        if (duration < 1) {
            result.put(DURATION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DURATION_FORMAT);
        }

        return result;
    }

    public Map<String, String> validateTags(List<TagDto> tags) {
        Map<String, String> result = new HashMap<>();

        for (TagDto tag : tags) {
            Map<String, String> validations = tagDtoValidator.validate(tag);
            if (!validations.isEmpty()) {
                result.putAll(validations);
            }
        }

        return result;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
