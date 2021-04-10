package com.epam.esm.validator.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.util.ResourceBundleMessage;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private static final int MIN_DESCRIPTION_LENGTH = 2;
    private static final int MAX_DESCRIPTION_LENGTH = 255;
    private final TagValidator tagValidator;

    public CertificateValidatorImpl(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public Map<String, String> validateForCreate(Certificate certificate) {
        Map<String, String> fails = new HashMap<>();
        fails.putAll(validateName(certificate.getName()));
        fails.putAll(validateDescription(certificate.getDescription()));
        fails.putAll(validatePrice(certificate.getPrice()));
        fails.putAll(validateDuration(certificate.getDuration()));
        fails.putAll(validateTags(certificate.getTags()));

        return fails;
    }

    @Override
    public Map<String, String> validateForUpdate(Certificate certificate) {
        Map<String, String> fails = new HashMap<>();
        if (StringUtils.isNotBlank(certificate.getName())) {
            fails.putAll(validateName(certificate.getName()));
        }
        if (StringUtils.isNotBlank(certificate.getDescription())) {
            fails.putAll(validateDescription(certificate.getDescription()));
        }
        if (Objects.nonNull(certificate.getPrice())) {
            fails.putAll(validatePrice(certificate.getPrice()));
        }
        if (Objects.nonNull(certificate.getDuration())) {
            fails.putAll(validateDuration(certificate.getDuration()));
        }
        if (CollectionUtils.isNotEmpty(certificate.getTags())) {
            fails.putAll(validateTags(certificate.getTags()));
        }

        return fails;
    }

    public Map<String, String> validateName(String name) {
        Map<String, String> fails = new HashMap<>();

        if (StringUtils.isBlank(name)) {
            fails.put(NAME_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_NAME_EMPTY);
            return fails;
        }
        name = name.trim();
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            fails.put(NAME_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_NAME_FORMAT);
        }

        return fails;
    }

    public Map<String, String> validateDescription(String description) {
        Map<String, String> fails = new HashMap<>();

        if (StringUtils.isBlank(description)) {
            fails.put(DESCRIPTION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DESCRIPTION_EMPTY);
            return fails;
        }
        description = description.trim();
        if (description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH) {
            fails.put(DESCRIPTION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DESCRIPTION_FORMAT);
        }

        return fails;
    }

    public Map<String, String> validatePrice(Integer price) {
        Map<String, String> fails = new HashMap<>();
        if (Objects.isNull(price)) {
            fails.put(PRICE_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_PRICE_EMPTY);
            return fails;
        }
        if (price < MIN_PRICE) {
            fails.put(PRICE_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_PRICE_FORMAT);
        }

        return fails;
    }

    public Map<String, String> validateDuration(Integer duration) {
        Map<String, String> fails = new HashMap<>();
        if (Objects.isNull(duration)) {
            fails.put(DESCRIPTION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DURATION_EMPTY);
            return fails;
        }
        if (duration < MIN_DURATION) {
            fails.put(DURATION_ATTRIBUTE, ResourceBundleMessage.CERTIFICATE_DURATION_FORMAT);
        }

        return fails;
    }

    public Map<String, String> validateTags(List<Tag> tags) {
        Map<String, String> fails = new HashMap<>();
        tags.stream().map(tagValidator::validate).forEach(fails::putAll);
        return fails;
    }
}
