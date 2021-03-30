package com.epam.esm.controller.dto;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;

import java.time.LocalDate;
import java.util.List;

public class CertificateDto {
    private int id;
    private String name;
    private String description;
    private int price;
    private int duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    private List<Tag> tags;

    public CertificateDto() {
    }

    public CertificateDto(int id, String name, String description, int price, int duration,
                          LocalDate createDate, LocalDate lastUpdateDate, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public CertificateDto(Certificate certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.price = certificate.getPrice();
        this.duration = certificate.getDuration();
        this.createDate = certificate.getCreateDate();
        this.lastUpdateDate = certificate.getLastUpdateDate();
        this.tags = certificate.getTags();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
