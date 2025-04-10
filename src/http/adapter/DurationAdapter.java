package http.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null || duration.equals(Duration.ZERO)) {
            jsonWriter.value(0);
        } else {
            jsonWriter.value(duration.toMinutes());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        long minutes = jsonReader.nextLong();
        if (minutes == 0) {
            return Duration.ZERO;
        } else {
            return Duration.ofMinutes(minutes);
        }
    }
}