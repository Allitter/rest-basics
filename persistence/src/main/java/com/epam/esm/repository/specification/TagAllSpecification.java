package com.epam.esm.repository.specification;

import com.epam.esm.model.Tag;

public class TagAllSpecification implements Specification<Tag> {

    @Override
    public String query() {
        return "SELECT * FROM tag";
    }
}
