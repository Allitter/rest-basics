package com.epam.esm.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

}
