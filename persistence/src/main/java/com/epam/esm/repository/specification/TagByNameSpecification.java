package com.epam.esm.repository.specification;

import com.epam.esm.model.Tag;

public class TagByNameSpecification implements Specification<Tag> {
    private final String name;

    public TagByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public String query() {
        return "select * from tag where name = ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{name};
    }
}
