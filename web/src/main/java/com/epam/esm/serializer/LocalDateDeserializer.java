package com.epam.esm.serializer;

import com.epam.esm.util.DateUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.regex.Pattern;


public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int THIRD_INDEX = 2;
    private static final String INVALID_DATE_FORMAT_EXCEPTION_MESSAGE = "Invalid date format";
    private static final String DASH_SPLITERATOR = "-";
    private static final String DOT_SPLITERATOR = ".";
    // yyyy-mm-dd
    Pattern iso8601DatePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    // dd.mm.yyyy
    Pattern ruPattern = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");
    // dd-mm-yyyy
    Pattern enPattern = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String date = json.getAsString().trim();

        if (iso8601DatePattern.matcher(date).matches()) {
            return parseDate(date, DASH_SPLITERATOR, FIRST_INDEX, SECOND_INDEX, THIRD_INDEX);
        } else if (ruPattern.matcher(date).matches()) {
            return parseDate(date, DOT_SPLITERATOR, THIRD_INDEX, SECOND_INDEX, FIRST_INDEX);
        } else if (enPattern.matcher(date).matches()) {
            return parseDate(date, DASH_SPLITERATOR, THIRD_INDEX, SECOND_INDEX, FIRST_INDEX);
        } else {
            throw new JsonParseException(INVALID_DATE_FORMAT_EXCEPTION_MESSAGE);
        }
    }

    private LocalDate parseDate(String date, String spliterator, int yearIndex, int monthIndex, int dayIndex) {
        String[] components = date.split(spliterator);
        int year = Integer.parseInt(components[yearIndex]);
        int month = Integer.parseInt(components[monthIndex]);
        int day = Integer.parseInt(components[dayIndex]);

        if (!DateUtils.isValidDate(year, month, day)) {
            throw new JsonParseException(INVALID_DATE_FORMAT_EXCEPTION_MESSAGE);
        }

        return LocalDate.of(year, month, day);
    }
}
