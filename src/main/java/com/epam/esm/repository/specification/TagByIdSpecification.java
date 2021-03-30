package com.epam.esm.repository.specification;

import com.epam.esm.model.Tag;

public class TagByIdSpecification implements Specification<Tag> {
    private final int id;

    public TagByIdSpecification(int id) {
        this.id = id;
    }

    @Override
    public String query() {
        return "select * from tag where id = ?;";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{id};
    }
}
