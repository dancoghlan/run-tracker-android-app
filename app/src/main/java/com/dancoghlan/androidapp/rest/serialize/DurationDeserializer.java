package com.dancoghlan.androidapp.rest.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.joda.time.Duration;

import java.io.IOException;

import static com.dancoghlan.androidapp.util.DateUtils.timeToDuration;

public class DurationDeserializer extends JsonDeserializer<Duration> {


    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String time = p.readValueAs(String.class);
        return timeToDuration(time);
    }

}
