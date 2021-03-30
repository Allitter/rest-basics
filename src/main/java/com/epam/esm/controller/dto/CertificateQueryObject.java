package com.epam.esm.controller.dto;

import java.util.Objects;
import java.util.StringJoiner;

public class CertificateQueryObject {
    public static final CertificateQueryObject EMPTY = new CertificateQueryObject();
    private final String name;
    private final String tagName;
    private final String description;
    private final String sortName;
    private final String sortDate;

    public CertificateQueryObject(String name, String tagName, String description, String sortName, String sortDate) {
        this.name = name;
        this.tagName = tagName;
        this.description = description;
        this.sortName = sortName;
        this.sortDate = sortDate;
    }

    private CertificateQueryObject() {
        this.name = null;
        this.tagName = null;
        this.description = null;
        this.sortName = null;
        this.sortDate = null;
    }

    public String getName() {
        return name;
    }

    public String getTagName() {
        return tagName;
    }

    public String getDescription() {
        return description;
    }

    public String getSortName() {
        return sortName;
    }

    public String getSortDate() {
        return sortDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateQueryObject that = (CertificateQueryObject) o;
        return Objects.equals(name, that.name)
                && Objects.equals(tagName, that.tagName)
                && Objects.equals(description, that.description)
                && Objects.equals(sortName, that.sortName)
                && Objects.equals(sortDate, that.sortDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tagName, description, sortName, sortDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CertificateQueryObject.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("tagName='" + tagName + "'")
                .add("description='" + description + "'")
                .add("sortName='" + sortName + "'")
                .add("sortDate='" + sortDate + "'")
                .toString();
    }
}
