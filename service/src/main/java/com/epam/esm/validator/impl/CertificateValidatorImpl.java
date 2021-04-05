package com.epam.esm.validator.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.util.ResourceBundleMessage;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.TagValidator;
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
        if (!isEmpty(certificate.getName())) {
            result.putAll(validateName(certificate.getName()));
        }
        if (!isEmpty(certificate.getDescription())) {
            result.putAll(validateDescription(certificate.getDescription()));
        }
        if (certificate.getDuration() != 0) {
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

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
