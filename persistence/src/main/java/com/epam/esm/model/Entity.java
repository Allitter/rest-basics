package com.epam.esm.model;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class Entity {
    private final long id;

    protected Entity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Entity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
