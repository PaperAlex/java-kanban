package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDatetime) throws IOException {
        if (localDatetime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDatetime.format(DATE_TIME_FORMATTER));
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String nullCheck = jsonReader.nextString();
        if (nullCheck.equals("")) {
            return null;
        }
        return LocalDateTime.parse(nullCheck, DATE_TIME_FORMATTER);
    }

}
