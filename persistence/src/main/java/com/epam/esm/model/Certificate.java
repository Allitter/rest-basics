package com.epam.esm.model;

import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.hash;
import static java.util.Objects.nonNull;

public class Certificate extends Entity {
    private final String name;
    private final String description;
    private final int price;
    private final int duration;
    private final LocalDate createDate;
    private final LocalDate lastUpdateDate;
    private final List<Tag> tags;

    private Certificate(Builder builder) {
        super(builder.id);
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.duration = builder.duration;
        this.createDate = builder.createDate;
        this.lastUpdateDate = builder.lastUpdateDate;
        this.tags = Collections.unmodifiableList(builder.tags);
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
        return new ArrayList<>(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return price == that.price
                && duration == that.duration
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate)
                && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return hash(name, description, price, duration, createDate, lastUpdateDate, tags);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Certificate.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("duration=" + duration)
                .add("createDate=" + createDate)
                .add("lastUpdateDate=" + lastUpdateDate)
                .add("tags=" + tags)
                .toString();
    }

    public static class Builder {
        private int id;
        private String name;
        private String description;
        private int price;
        private int duration;
        private LocalDate createDate;
        private LocalDate lastUpdateDate;
        private List<Tag> tags;


        public Builder() {
            this.tags = new ArrayList<>();
        }

        public Builder(Certificate certificate) {
            super();
            this.id = certificate.getId();
            this.name = certificate.getName();
            this.description = certificate.getDescription();
            this.price = certificate.getPrice();
            this.duration = certificate.getDuration();
            this.createDate = certificate.getCreateDate();
            this.lastUpdateDate = certificate.getLastUpdateDate();
            this.tags = certificate.getTags();
        }

        public static Certificate merge(Certificate to, Certificate from) {
            Builder builder = new Builder(to);
            if (nonNull(from.name)) {
                builder.setName(from.name);
            }
            if (nonNull(from.description)) {
                builder.setDescription(from.description);
            }
            if (from.price != 0) {
                builder.setPrice(from.price);
            }
            if (from.duration != 0) {
                builder.setDuration(from.duration);
            }
            if (nonNull(from.createDate)) {
                builder.setCreateDate(from.createDate);
            }
            if (from.tags != null && !from.tags.isEmpty()) {
                builder.setTags(from.tags);
            }

            return builder.build();
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setCreateDate(LocalDate createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdateDate(LocalDate lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder setTags(List<Tag> tags) {
            if (tags != null) {
                this.tags = tags;
            }
            return this;
        }

        public Certificate build() {
            return new Certificate(this);
        }
    }
}
