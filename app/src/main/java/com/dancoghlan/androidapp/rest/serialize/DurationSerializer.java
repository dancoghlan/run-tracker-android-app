package com.dancoghlan.androidapp.rest.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.joda.time.Duration;

import java.io.IOException;

import static com.dancoghlan.androidapp.util.DateUtils.formatDuration;

public class DurationSerializer extends StdSerializer<Duration> {

    public DurationSerializer() {
        this(null);
    }

    public DurationSerializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public void serialize(Duration duration, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(formatDuration(duration));
    }

}
