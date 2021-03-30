package com.epam.esm.service.stream.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.service.stream.ServiceStream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagStreamImpl implements ServiceStream<Tag> {
    private Stream<Tag> tagStream;

    public TagStreamImpl(List<Tag> tags) {
        this.tagStream = tags.stream();
    }

    @Override
    public List<Tag> get() {
        return tagStream.collect(Collectors.toList());
    }
}
