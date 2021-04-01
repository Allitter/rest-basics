package com.epam.esm.repository.specification;

import com.epam.esm.model.Tag;

public class TagByNameOrIdSpecification implements Specification<Tag> {
    private final int id;
    private final String name;

    public TagByNameOrIdSpecification(String name, int id) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String query() {
        return "select * from tag where name = ? or id = ?;";
    }

    @Override
    public Object[] getArgs() {
        return new Object[] {name, id};
    }
}
