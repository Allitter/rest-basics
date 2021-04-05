package com.epam.esm.validator.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.util.ResourceBundleMessage;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateValidatorImpl implements CertificateValidator {
    private static final String DURATION_ATTRIBUTE = "duration";
    private static final String PRICE_ATTRIBUTE = "price";
    private static final String DESCRIPTION_ATTRIBUTE = "description";
    private static final String NAME_ATTRIBUTE = "certificate_name";
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 64;
    private static final int MIN_DURATION = 1;
    private static final int MIN_PRICE = 0;
    private static final int ZERO = 0;
    private static final int MIN_DESCRIPTION_LENGTH = 2;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private final TagValidator tagValidator;

    public CertificateValidatorImpl(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public Map<String, String> validateForCreate(Certificate certificate) {
        Map<String, String> result = new HashMap<>();
        result.putAll(validateName(certificate.getName()));
        result.putAll(validateDescription(certificate.getDescription()));
        result.putAll(validatePrice(certificate.getPrice()));
        result.putAll(validateDuration(certificate.getDuration()));
        result.putAll(validateTags(certificate.getTags()));

        return result;
    }

    @Override
    public Map<String, String> validateForUpdate(Certificate certificate) {
        Map<String, String> result = new HashMap<>();
        if (!StringUtils.isBlank(certificate.getName())) {
            result.putAll(validateName(certificate.getName()));
        }
        if (!StringUtils.isBlank(certificate.getDescription())) {
            result.putAll(validateDescription(certificate.getDescription()));
        }
        if (certificate.getDuration() != ZERO) {
            result.putAll(validateDuration(certificate.getDuration()));
        }
        if (certificate.getTags() != null && !certificate.getTags().isEmpty()) {
            result.putAll(validateTags(certificate.getTags()));
        }
        result.putAll(validatePrice(certificate.getPrice()));

        return result;
    }

    public Map<String, String> validateName(String name) {
        Map<String, String> result = new HashMap<>();

        if (StringUtils.isBlank(name)) {
            result.put(NAME_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_NAME_EMPTY);
            return result;
        }
        name = name.trim();
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            result.put(NAME_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_NAME_FORMAT);
        }

        return result;
    }

    public Map<String, String> validateDescription(String description) {
        Map<String, String> result = new HashMap<>();

        if (StringUtils.isBlank(description)) {
            result.put(DESCRIPTION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DESCRIPTION_EMPTY);
            return result;
        }
        description = description.trim();
        if (description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH) {
            result.put(DESCRIPTION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DESCRIPTION_FORMAT);
        }

        return result;
    }

    public Map<String, String> validatePrice(int price) {
        Map<String, String> result = new HashMap<>();
        if (price < MIN_PRICE) {
            result.put(PRICE_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_PRICE_FORMAT);
        }

        return result;
    }

    public Map<String, String> validateDuration(int duration) {
        Map<String, String> result = new HashMap<>();
        if (duration < MIN_DURATION) {
            result.put(DURATION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DURATION_FORMAT);
        }

        return result;
    }

    public Map<String, String> validateTags(List<Tag> tags) {
        Map<String, String> result = new HashMap<>();

        for (Tag tag : tags) {
            Map<String, String> validations = tagValidator.validate(tag);
            if (!validations.isEmpty()) {
                result.putAll(validations);
            }
        }

        return result;
    }
}
