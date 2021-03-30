package com.epam.esm.controller.dto;

import com.epam.esm.model.Tag;

import java.util.Objects;
import java.util.StringJoiner;

public class TagDto {
    private final int id;
    private final String name;

    public TagDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagDto(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TagDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return id == tagDto.id && Objects.equals(name, tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
