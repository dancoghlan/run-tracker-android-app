package com.dancoghlan.androidapp.rest.serialize;

import com.dancoghlan.androidapp.model.Pace;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PaceDeserializer extends JsonDeserializer<Pace> {


    @Override
    public Pace deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String time = p.readValueAs(String.class);
        return new Pace(time);
    }

}
